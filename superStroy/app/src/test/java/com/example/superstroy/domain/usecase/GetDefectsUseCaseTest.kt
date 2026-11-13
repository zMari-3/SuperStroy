package com.example.superstroy.domain.usecase

import com.example.superstroy.domain.model.Defect
import com.example.superstroy.domain.model.DefectStatus
import com.example.superstroy.domain.repository.FakeDefectRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.test.assertEquals

class GetDefectsUseCaseTest {
    private var repository = FakeDefectRepository()
    private var useCase = GetDefectsUseCase(repository)

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetDefectsUseCase(repository)
    }

    @Test
    fun `Возвращаем список дефектов`() = runTest {

        val expectedDefects = listOf(
            Defect(
                "1",
                "Трещина",
                "Описание",
                "Иванов",
                LocalDate.now(),
                LocalDateTime.now(),
                0.0,
                0.0,
                null,
                DefectStatus.OPEN
            )
        )
        every { repository.getDefects() } returns flowOf(expectedDefects)

        val result = useCase().first()

        assertEquals(expectedDefects, result)
    }
}