import { assertEquals, assertExists } from "jsr:@std/assert@1";

import { handler } from "../recognize-speech/index.ts";

//Тестовые данные
const createMockFile = (size: number, type: string) => {
  return new File([new Uint8Array(size)], "test.m4a", { type });
};

async function invokeHandler(body: FormData | null, method = "POST") {
  const req = new Request("http://localhost:54321/functions/v1/recognize-speech", {
    method,
    body,
  });
}

Deno.test("1. OPTIONS-запрос возвращает 200", async () => {
  const req = new Request("http://localhost", { method: "OPTIONS" });
  const res = await handler(req);
  assertEquals(res.status, 200);
  assertEquals(res.headers.get("Access-Control-Allow-Origin"), "*");
});

Deno.test("2. POST-запрос без файла возвращает 400", async () => {
  const req = new Request("http://localhost", {
    method: "POST",
    body: new FormData(), // Пустая форма
  });
  const res = await handler(req);
  assertEquals(res.status, 400);
  const body = await res.json();
  assertEquals(body.code, "MISSING_FILE");
  assertExists(body.timestamp);
});

Deno.test("3. Слишком большой файл (11 МБ) возвращает 413", async () => {
  const formData = new FormData();
  formData.append("audio", createMockFile(11 * 1024 * 1024, "audio/mp4"));
  const req = new Request("http://localhost", { method: "POST", body: formData });
  const res = await handler(req);
  assertEquals(res.status, 413);
  const body = await res.json();
  assertEquals(body.code, "FILE_TOO_LARGE");
});

Deno.test("4. Неподдерживаемый тип файла возвращает 415", async () => {
  const formData = new FormData();
  formData.append("audio", createMockFile(1024, "text/plain"));
  const req = new Request("http://localhost", { method: "POST", body: formData });
  const res = await handler(req);
  assertEquals(res.status, 415);
  const body = await res.json();
  assertEquals(body.code, "UNSUPPORTED_MEDIA_TYPE");
});

Deno.test("5. Пустой файл вызывает ошибку сервера (500)", async () => {
  const formData = new FormData();
  formData.append("audio", createMockFile(0, "audio/mp4"));
  const req = new Request("http://localhost", { method: "POST", body: formData });
  const res = await handler(req);
  // Сервер пропустит пустой файл по размеру, но Speechmatics вернет ошибку при отправке
  assertEquals(res.status, 500);
});

Deno.test("6. CORS-заголовки присутствуют в ответах с ошибкой", async () => {
  const req = new Request("http://localhost", {
    method: "POST",
    body: new FormData(),
  });
  const res = await handler(req);
  assertEquals(res.headers.get("Access-Control-Allow-Origin"), "*");
});

Deno.test("7. Структура ответа с ошибкой валидна", async () => {
  const req = new Request("http://localhost", {
    method: "POST",
    body: new FormData(),
  });
  const res = await handler(req);
  const body = await res.json();
  assertExists(body.error);
  assertExists(body.code);
  assertExists(body.timestamp);
  assertEquals(typeof body.error, "string");
});

Deno.test("8. Валидный тип 'audio/wav' проходит проверку и возвращает 200", async () => {
  const formData = new FormData();
  formData.append("audio", createMockFile(1024, "audio/wav"));
  const req = new Request("http://localhost", { method: "POST", body: formData });
  
  const originalFetch = globalThis.fetch;
  // Имитируем успешные ответы от Speechmatics
  globalThis.fetch = async (input: RequestInfo | URL) => {
    const url = input.toString();
    if (url.includes("/transcript")) {
      return new Response(JSON.stringify({ results: [{ type: "word", alternatives: [{ content: "Тест" }] }] }), { status: 200 });
    }
    if (url.includes("/jobs/")) {
      return new Response(JSON.stringify({ job: { status: "done" } }), { status: 200 });
    }
    return new Response(JSON.stringify({ id: "fake-job-id" }), { status: 200 });
  };
  
  const res = await handler(req);
  assertEquals(res.status, 200);
  const body = await res.json();
  assertEquals(body.text, "Тест");
  
  globalThis.fetch = originalFetch;
});

Deno.test("9. Валидный тип 'audio/webm' проходит проверку и возвращает 200", async () => {
  const formData = new FormData();
  formData.append("audio", createMockFile(1024, "audio/webm"));
  const req = new Request("http://localhost", { method: "POST", body: formData });
  
  const originalFetch = globalThis.fetch;
  globalThis.fetch = async () => {
      return new Response(JSON.stringify({ results: [{ type: "word", alternatives: [{ content: "Тест" }] }], job: { status: "done" }, id: "fake-id" }), { status: 200 });
  };
  
  const res = await handler(req);
  assertEquals(res.status, 200);
  
  globalThis.fetch = originalFetch;
});

Deno.test("10. Внутренняя ошибка имеет правильный формат", async () => {
  const formData = new FormData();
  formData.append("audio", createMockFile(1024, "audio/mp4"));
  const req = new Request("http://localhost", { method: "POST", body: formData });
  
  const originalFetch = globalThis.fetch;
  // Имитируем падение сети
  globalThis.fetch = async () => { throw new Error("Network error"); };
  
  const res = await handler(req);
  assertEquals(res.status, 500);
  const body = await res.json();
  assertEquals(body.code, "INTERNAL_ERROR");
  assertEquals(body.error, "Network error");
  
  globalThis.fetch = originalFetch;
});

