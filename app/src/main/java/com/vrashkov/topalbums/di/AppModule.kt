package com.vrashkov.topalbums.di

import android.content.Context
import com.vrashkov.core.util.DataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideDataSource(@ApplicationContext appContext: Context): DataStoreManager =
        DataStoreManager(appContext)
}