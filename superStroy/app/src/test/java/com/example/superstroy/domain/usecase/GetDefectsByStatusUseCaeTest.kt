package com.example.superstroy.domain.usecase

import com.example.superstroy.domain.model.DefectStatus
import com.example.superstroy.domain.repository.DefectRepository
import com.example.superstroy.domain.repository.FakeDefectRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetDefectsByStatusUseCaeTest {
    private lateinit var repository: FakeDefectRepository
    private lateinit var useCase: GetDefectsByStatusUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetDefectsByStatusUseCase(repository)
    }

    @Test
    fun `Передаем правильный статус`() = runTest {
        val status = DefectStatus.CLOSED
        every { repository.getDefectByStatus(status) } returns flowOf(emptyList())

        useCase(status).first()

        verify(exactly = 1) { repository.getDefectByStatus(status) }
    }
}
