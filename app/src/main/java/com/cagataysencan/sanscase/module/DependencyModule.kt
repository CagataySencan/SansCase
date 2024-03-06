package com.cagataysencan.sanscase.module

import android.content.Context
import com.cagataysencan.sanscase.database.AppDatabase
import com.cagataysencan.sanscase.database.BaseDao
import com.cagataysencan.sanscase.database.DatabaseRepository
import com.cagataysencan.sanscase.service.ApiRepository
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

@Module
@InstallIn(ViewModelComponent::class)
object MainViewModelModule {

    @ViewModelScoped
    @Provides
    fun providesDatabaseRepository(
        baseDao: BaseDao,
    ): DatabaseRepository {
        return DatabaseRepository(baseDao)
    }

    @ViewModelScoped
    @Provides
    fun providesApiRepository(): ApiRepository {
        return ApiRepository(ApiService())
    }
}

@Module
@InstallIn(SingletonComponent::class)
object MainFragmentModule {
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ) : AppDatabase {
        return AppDatabase.getDatabase(context)!!
    }
    @Provides
    fun provideChannelDao(appDatabase: AppDatabase): BaseDao {
        return appDatabase.getMatchDao()
    }
    @Singleton
    @Provides
    fun providesMainViewModel(
        databaseRepository: DatabaseRepository,
        apiRepository: ApiRepository): MainViewModel {
        return MainViewModel(databaseRepository, apiRepository)
    }

    @Singleton
    @Provides
    fun providesDetailViewModel(): DetailViewModel {
        return DetailViewModel()
    }
}
