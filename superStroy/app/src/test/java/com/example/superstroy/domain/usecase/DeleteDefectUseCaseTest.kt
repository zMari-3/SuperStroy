package com.example.superstroy.domain.usecase

import com.example.superstroy.domain.model.Defect
import com.example.superstroy.domain.model.DefectStatus
import com.example.superstroy.domain.repository.FakeDefectRepository
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DeleteDefectUseCaseTest {
    private lateinit var fakeRepository: FakeDefectRepository
    private lateinit var useCase: DeleteDefectUseCase

    //Выполняем каждый раз, с новым состоянием для каждого теста
    @Before
    fun setUp() {
        fakeRepository = FakeDefectRepository()
        useCase = DeleteDefectUseCase(fakeRepository)
    }

    @Test
    fun `Удаляем существующий дефект`() = runTest()
    {
        val testDefect = Defect(
            id = "123", title = "Трещина", description = "", responsible = "Иванов",
            term = LocalDate.now(), createdDate = LocalDateTime.now(),
            latitude = null, longitude = null, photoUrl = null, status = DefectStatus.OPEN
        )

        fakeRepository.setDefects(listOf(testDefect))

        val result = useCase(testDefect)

        assertTrue { result}
        // Проверяем, что список стал пустым
        assertTrue(fakeRepository.getDefectById("123") == null)
    }

    @Test
    fun `Удаляем несуществующий дефект`() = runTest ()
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