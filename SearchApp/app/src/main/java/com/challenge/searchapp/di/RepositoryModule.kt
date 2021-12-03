package com.challenge.searchapp.di

import com.challenge.searchapp.repository.ItemsRepository
import com.challenge.searchapp.repository.ItemsRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun userRepository(repo: ItemsRepositoryImp): ItemsRepository
}








