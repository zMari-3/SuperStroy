package com.example.superstroy.domain.usecase

import com.example.superstroy.domain.model.Defect
import com.example.superstroy.domain.model.DefectStatus
import com.example.superstroy.domain.repository.FakeDefectRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GetDefectsByIdUseCaseTest {
    private lateinit var repository: FakeDefectRepository
    private lateinit var useCase: GetDefectByIdUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetDefectByIdUseCase(repository)
    }

    @Test
    fun `Возвращаем дефект принахождении`() = runTest {
        val defect = Defect(
            "1",
            "Трещина",
            null,
            "Иванов",
            LocalDate.now(),
            LocalDateTime.now(),
            0.0,
            0.0,
            null,
            DefectStatus.OPEN
        )
        coEvery { repository.getDefectById("1") } returns defect

        val result = useCase("1")

        assertEquals(defect, result)
    }

    @Test
    fun `Возвращаем null если дефект не найден`() = runTest {
        coEvery { repository.getDefectById("999") } returns null

        val result = useCase("999")

        assertNull(result)
    }

    @Test
    fun `Возвращаем дефект при исключении`() = runTest {
        coEvery { repository.getDefectById(any()) } throws Exception("DB Error")

        val result = useCase("1")

        assertNull(result)
    }
}
