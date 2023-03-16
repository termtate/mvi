package com.example.myapplicationg.di

import com.example.myapplicationg.usecase.FormatDateUseCase
import com.example.myapplicationg.usecase.PublishItemConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideFormDateUseCase(): FormatDateUseCase = FormatDateUseCase()

    @Provides
    @Singleton
    fun provideItemConverterUseCase(
        formatDateUseCase: FormatDateUseCase
    ): PublishItemConverter = PublishItemConverter(formatDateUseCase)

//    @Provides
//    @Singleton
//    fun provideProfileUseCase(
//        publishItemConverter: PublishItemConverter,
//        userInfoRepository: UserInfoRepository,
//
//    ) = ProfileUseCase()
}