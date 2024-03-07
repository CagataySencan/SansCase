package com.cagataysencan.sanscase.module

import android.content.Context
import com.cagataysencan.sanscase.database.AppDatabase
import com.cagataysencan.sanscase.database.MatchDao
import com.cagataysencan.sanscase.repository.MatchRepository
import com.cagataysencan.sanscase.service.ApiService
import com.cagataysencan.sanscase.viewmodel.DetailViewModel
import com.cagataysencan.sanscase.viewmodel.MainViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Dependency Injection Modules
@Module
@InstallIn(ViewModelComponent::class)
object MainViewModelModule {
    @ViewModelScoped
    @Provides
    fun provideMatchRepository(
        matchDao: MatchDao,
        apiService: ApiService
    ): MatchRepository {
        return MatchRepository(matchDao,apiService)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object MainFragmentModule {
    @Singleton
    @Provides
    fun providesMainViewModel(
        matchRepository: MatchRepository): MainViewModel {
        return MainViewModel(matchRepository)
    }

    @Singleton
    @Provides
    fun providesDetailViewModel(): DetailViewModel {
        return DetailViewModel()
    }

    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ) : AppDatabase {
        return AppDatabase.getDatabase(context)!!
    }

    @Provides
    fun provideMatchDao(appDatabase: AppDatabase): MatchDao {
        return appDatabase.getMatchDao()
    }

    @Provides
    fun provideApiService(): ApiService {
        return ApiService()
    }
}
