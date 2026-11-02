// Follow this setup guide to integrate the Deno language server with your editor:
// https://deno.land/manual/getting_started/setup_your_environment
// This enables autocomplete, go to definition, etc.

const corsHeaders = {
  'Access-Control-Allow-Origin': '*',
  'Access-Control-Allow-Headers': 'authorization, x-client-info, apikey, content-type',
};

Deno.serve(async (req) => {
  if (req.method === 'OPTIONS') {
    return new Response('ok', { headers: corsHeaders });
  }

  try {
    const formData = await req.formData();
    const audioFile = formData.get('audio') as File;

    if (!audioFile) {
      return new Response(JSON.stringify({ error: 'Файл не найден' }), {
        status: 400,
        headers: { ...corsHeaders, 'Content-Type': 'application/json' },
      });
    }

    const arrayBuffer = await audioFile.arrayBuffer();

    // Формируем multipart/form-data для Speechmatics
    const smForm = new FormData();
    
    // Файл аудио
    smForm.append('data_file', new Blob([arrayBuffer]), audioFile.name);
    
    const config = {
      type: 'transcription',
      transcription_config: {
        language: 'ru',
        operating_point: 'enhanced',
      },
    };
    smForm.append('config', JSON.stringify(config));

    // Отправляем в Speechmatics 
    const response = await fetch('https://eu1.asr.api.speechmatics.com/v2/jobs/', {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${Deno.env.get('SPEECHMATICS_API_KEY')}`,
        // Content-Type fetch сам добавит boundary для multipart
      },
      body: smForm,
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(`Speechmatics API error: ${response.status} - ${errorText}`);
    }

    const job = await response.json();
    const jobId = job.id;

    // Опрос статуса задачи
    let transcriptText = '';
    for (let i = 0; i < 30; i++) {
      await new Promise(r => setTimeout(r, 1000));
      
      const statusRes = await fetch(`https://eu1.asr.api.speechmatics.com/v2/jobs/${jobId}`, {
        headers: { 'Authorization': `Bearer ${Deno.env.get('SPEECHMATICS_API_KEY')}` },
      });
      const status = await statusRes.json();

      if (status.job.status === 'done') {
        const transcriptRes = await fetch(`https://eu1.asr.api.speechmatics.com/v2/jobs/${jobId}/transcript`, {
          headers: { 'Authorization': `Bearer ${Deno.env.get('SPEECHMATICS_API_KEY')}` },
        });
        const transcript = await transcriptRes.json();
        transcriptText = transcript.results
          .filter((r: any) => r.type === 'word')
          .map((r: any) => r.alternatives[0].content)
          .join(' ');
        break;
      } else if (status.job.status === 'rejected' || status.job.status === 'failed') {
        throw new Error(`Speechmatics job failed: ${JSON.stringify(status)}`);
      }
    }

    if (!transcriptText) {
      throw new Error('Превышено время ожидания транскрипции');
    }

    return new Response(JSON.stringify({ text: transcriptText }), {
      headers: { ...corsHeaders, 'Content-Type': 'application/json' },
    });
  } catch (error) {
    return new Response(JSON.stringify({ error: error.message }), {
      status: 500,
      headers: { ...corsHeaders, 'Content-Type': 'application/json' },
    });
  }
});