package com.example.superstroy.domain.usecase

import com.example.superstroy.domain.repository.FakeDefectRepository
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ParseVoiceCommandUseCaseTest {
    private lateinit var useCase: ParseVoiceCommandUseCase

    //Выполняем каждый раз, с новым состоянием для каждого теста
    @Before
    fun setUp() {
        useCase = ParseVoiceCommandUseCase()
    }

    @Test
    fun `Парсим корректную строку`()
    {
        val result = useCase("Дыра в стене, Киркоров Ф.Б., 01.05.2026")

        assertEquals("Дыра в стене", result?.title)
        assertEquals("Киркоров Ф.Б.", result?.responsible)
        assertEquals("01.05.2026", result?.term)
    }

    @Test
    fun `Парсим некорректную строку`()
    {
        val result = useCase("Дыра")

        assertEquals("Дыра", result?.title)
        assertEquals("", result?.responsible)
        //Ожидаем пустую строку, а не краш
    }

    @Test
    fun `Парсим пустую строку`()
    {
        val result = useCase("  ")
        assertNull(result)
    }

}