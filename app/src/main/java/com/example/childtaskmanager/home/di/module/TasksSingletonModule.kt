package com.example.childtaskmanager.home.di.module

import com.example.childtaskmanager.core.data.db.AppDatabase
import com.example.childtaskmanager.core.di.module.CoreModule
import com.example.childtaskmanager.home.data.HomeRepositoryImpl
import com.example.childtaskmanager.home.data.mapper.DbScheduleToNetworkMapper
import com.example.childtaskmanager.home.data.mapper.DbTasksToNetworkMapper
import com.example.childtaskmanager.home.data.mapper.NetworkScheduleToDbMapper
import com.example.childtaskmanager.home.data.mapper.NetworkTasksToDbMapper
import com.example.childtaskmanager.home.data.network.HomeApi
import com.example.childtaskmanager.home.domain.repository.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module(includes = [CoreModule::class])
object TasksSingletonModule {

    @Singleton
    @Provides
    fun provideHomeRepository(
        retrofit: Retrofit,
        database: AppDatabase,
    ): HomeRepository {

        return HomeRepositoryImpl(
            homeApi = retrofit.create(HomeApi::class.java),
            tasksDao = database.tasksDao(),
            scheduleDao=database.scheduleDao(),
            mapNetworkTasksToDb = NetworkTasksToDbMapper()::map,
            mapDbTasksToNetwork = DbTasksToNetworkMapper::map,
            mapNetworkScheduleToDbMapper = NetworkScheduleToDbMapper()::map,
            mapDbScheduleToNetwork = DbScheduleToNetworkMapper()::map
        )
    }
}
