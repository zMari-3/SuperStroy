package com.example.superstroy.domain.usecase

import com.example.superstroy.domain.model.Defect
import com.example.superstroy.domain.model.DefectStatus
import com.example.superstroy.domain.repository.FakeDefectRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

class CloseDefectUseCaseWithFakeTest {
    private lateinit var fakeRepository: FakeDefectRepository
    private lateinit var useCase: CloseDefectUseCase

    //Выполняем каждый раз, с новым состоянием для каждого теста
    @Before
    fun setUp() {
        fakeRepository = FakeDefectRepository()
        useCase = CloseDefectUseCase(fakeRepository)
    }

    @Test
    fun `Закрываем дефект меняя статус дефекта`() = runTest {
        val testDefect = Defect(
            id = "123", title = "Трещина", description = "", responsible = "Иванов",
            term = LocalDate.now(), created_date = LocalDateTime.now(),
            latitude = null, longitude = null, photo_url = null, status = DefectStatus.OPEN
        )
        fakeRepository.setDefects(listOf(testDefect))

        val result = useCase("123")

        assertEquals(true, result)

        // Проверяем, что статус в фейковом репозитории реально изменился
        val updatedDefect = fakeRepository.getDefectById("123")
        assertEquals(DefectStatus.CLOSED, updatedDefect?.status)
    }

    @Test
    fun `Закрываем дефект возвращающий ложь при ошибке`() = runTest {
        // Настраиваем на выброс ошибки
        fakeRepository.shouldReturnError = true

        val result = useCase("123")

        assertEquals(false, result)
    }
}