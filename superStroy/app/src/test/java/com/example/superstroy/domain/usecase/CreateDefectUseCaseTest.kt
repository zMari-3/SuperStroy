package com.example.superstroy.domain.usecase

import com.example.superstroy.domain.repository.FakeDefectRepository
import com.example.superstroy.domain.repository.GeoLocation
import com.example.superstroy.domain.repository.LocationProvider
import com.example.superstroy.domain.repository.PhotoStorage
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import io.mockk.mockk
import java.time.LocalDate
import kotlin.test.assertNotNull
import kotlin.test.assertNull


class CreateDefectUseCaseTest {
    private lateinit var repository: FakeDefectRepository
    private  lateinit var locationProvider: LocationProvider
    private lateinit var photoStorage: PhotoStorage
    private lateinit var useCase: CreateDefectUseCase

    //Выполняем каждый раз, с новым состоянием для каждого теста
    @Before
    fun setUp() {
        repository = mockk()
        useCase = CreateDefectUseCase(repository, locationProvider, photoStorage)
    }

    @Test
    fun `Обьеденяем данные и создаём дефект`() = runTest {
        coEvery { photoStorage.savePhotoToLocal("temp_uri") } returns "local_path"
        coEvery { locationProvider.getCurrentLocation() } returns GeoLocation(55.75, 37.61)

        val result = useCase("Трещина", "Иванов", LocalDate.now(), "temp_uri")

        assertNotNull(result)
        coVerify(exactly = 1) { repository.insert(any()) }
    }

    @Test
    fun `Возвращаем null при исключении`() = runTest {
        coEvery { photoStorage.savePhotoToLocal(any()) } returns "local_path"
        coEvery { locationProvider.getCurrentLocation() } throws Exception("GPS Disabled")

        val result = useCase("Трещина", "Иванов", LocalDate.now(), "temp_uri")

        assertNull(result) // UseCase должен перехватить ошибку и вернуть null
    }
}