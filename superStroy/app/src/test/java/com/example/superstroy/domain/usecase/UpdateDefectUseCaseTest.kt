package com.example.superstroy.domain.usecase

import com.example.superstroy.domain.model.Defect
import com.example.superstroy.domain.model.DefectStatus
import com.example.superstroy.domain.repository.FakeDefectRepository
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.test.assertEquals

class UpdateDefectUseCaseTest {
    private lateinit var fakeRepository: FakeDefectRepository
    private lateinit var useCase: UpdateDefectUseCase

    //Выполняем каждый раз, с новым состоянием для каждого теста
    @Before
    fun setUp() {
        fakeRepository = FakeDefectRepository()
        useCase = UpdateDefectUseCase(fakeRepository)
    }

    @Test
    fun `Обновляем существующий дефект`() = runTest()
    {
        val testDefect = Defect(
            id = "123", title = "Трещина", description = "", responsible = "Иванов",
            term = LocalDate.now(), createdDate = LocalDateTime.now(),
            latitude = null, longitude = null, photoUrl = null, status = DefectStatus.OPEN
        )
        fakeRepository.setDefects(listOf(testDefect))

        // Меняем название
        val updatedDefect = testDefect.copy(title = "Новое название")

        val result = useCase(updatedDefect)

        assertTrue(result)

        // Проверяем, что в базе обновилось
        val defectInDb = fakeRepository.getDefectById("123")
        assertEquals("Новое название", defectInDb?.title)
    }

    @Test
    fun `Обновляем несуществующий дефект`() = runTest()
    {
        val testDefect = Defect(
            id = "", title = "", description = "", responsible = "",
            term = LocalDate.now(), createdDate = LocalDateTime.now(),
            latitude = null, longitude = null, photoUrl = null, status = DefectStatus.OPEN
        )

        val result = useCase(testDefect)

        assertFalse(result)
    }
}