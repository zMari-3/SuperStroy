package com.example.superstroy.data.di

import com.example.superstroy.data.repository.PhotoStorageImpl
import com.example.superstroy.data.repository.LocationProviderImpl
import com.example.superstroy.domain.repository.LocationProvider
import com.example.superstroy.domain.repository.PhotoStorage
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HardwareModule {

    @Binds
    @Singleton
    abstract fun bindLocationProvider(impl: LocationProviderImpl): LocationProvider

    @Binds
    @Singleton
    abstract fun bindPhotoStorage(impl: PhotoStorageImpl): PhotoStorage
}