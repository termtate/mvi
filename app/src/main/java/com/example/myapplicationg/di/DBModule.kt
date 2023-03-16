package com.example.myapplicationg.di

import android.content.Context
import androidx.room.Room
import com.example.myapplicationg.data.db.DbDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    @Provides
    @Singleton
    fun provideDB(
        @ApplicationContext context: Context
    ): DbDataSource {
        return Room
            .databaseBuilder(context, DbDataSource::class.java, "items.db")
            .build()
    }
}