package com.example.myapplicationg.di

import com.example.myapplicationg.data.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {

    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun providesIoContext(
        ioDispatcher: CoroutineDispatcher,
        authRepository: AuthRepository
    ): CoroutineContext = ioDispatcher + authRepository.authExceptionHandler
}