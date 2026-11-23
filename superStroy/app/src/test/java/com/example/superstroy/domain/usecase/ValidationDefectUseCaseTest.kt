package com.example.superstroy.domain.usecase

import com.example.superstroy.domain.repository.FakeDefectRepository
import io.mockk.mockk
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class ValidationDefectUseCaseTest {
    private lateinit var useCase: ValidateDefectUseCase

    //Выполняем каждый раз, с новым состоянием для каждого теста
    @Before
    fun setUp() {
        useCase = ValidateDefectUseCase()
    }

    @Test
    fun `Проверяем корректную строку`()
    {
        val result = useCase("Дыра в стене", "Киркоров Ф.Б.", "01.05.2026")

        assertTrue(result.isSuccess)
    }

    @Test
    fun `Проверяем строку с пустым названием`()
    {
        val result = useCase("", "Киркоров Ф.Б.", "01.05.2026")

        assertFalse(result.isSuccess)
        assertEquals("Название не может быть пустым", result.exceptionOrNull()?.message)
    }

    @Test
    fun `Проверяем строку с пустым отвественным`()
    {
        val result = useCase("Дыра", "", "01.05.2026")

        assertFalse(result.isSuccess)
        assertEquals("Укажите ответственного", result.exceptionOrNull()?.message)
    }

    @Test
    fun `Проверяем строку с пустым сроком`()
    {
        val result = useCase("Дыра", "Киркоров Ф.Б.", "")

        assertFalse(result.isSuccess)
        assertEquals("Укажите срок устранения", result.exceptionOrNull()?.message)
    }
}