package com.example.superstroy.data.di

import android.content.Context
import androidx.room.Room
import com.example.superstroy.data.local.AppDatabase
import com.example.superstroy.data.local.DefectDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "superstroy_database"
        ).build()
    }

    @Provides
    fun provideDefectDao(database: AppDatabase): DefectDao {
        return database.defectDao()
    }

    // Глобальный Scope для фоновой синхронизации, который живет столько же, сколько приложение
    @Provides
    @Singleton
    fun provideApplicationScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }
}