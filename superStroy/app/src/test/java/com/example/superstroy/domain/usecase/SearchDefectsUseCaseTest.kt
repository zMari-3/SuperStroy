package com.example.superstroy.domain.usecase

import com.example.superstroy.domain.repository.FakeDefectRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SearchDefectsUseCaseTest {

    private lateinit var repository: FakeDefectRepository
    private lateinit var useCase: SearchDefectsUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = SearchDefectsUseCase(repository)
    }

    @Test
    fun `Возвращаем пустой список и не вызываем репозиторий для пустого запроса`() = runTest {
        val result = useCase("   ").first()

        assertTrue(result.isEmpty())
        verify(exactly = 0) { repository.searchDefects(any()) }
    }

    @Test
    fun `Обрезанный запрос`() = runTest {
        every { repository.searchDefects("трещина") } returns flowOf(emptyList())

        useCase("  трещина  ").first()

        verify(exactly = 1) { repository.searchDefects("трещина") }
    }
}
