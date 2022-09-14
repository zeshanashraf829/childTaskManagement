package com.example.childtaskmanager.home.data

import Schedule
import TasksListResponse
import android.util.Log
import com.example.childtaskmanager.home.data.db.TaskDao
import com.example.childtaskmanager.home.data.dbModel.DbTasks
import com.example.childtaskmanager.home.data.network.HomeApi
import com.example.childtaskmanager.home.domain.error.HomeScreenError
import com.example.childtaskmanager.home.domain.repository.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import com.example.childtaskmanager.core.domain.Result
import com.example.childtaskmanager.home.data.db.ScheduleDao
import com.example.childtaskmanager.home.data.dbModel.DbSchedule
import com.example.childtaskmanager.home.data.mapper.NetworkScheduleToDbMapper
import com.google.gson.Gson
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class HomeRepositoryImpl constructor(
    private val homeApi: HomeApi,
    private val tasksDao: TaskDao,
    private val scheduleDao: ScheduleDao,
    private val mapNetworkTasksToDb: (networkOrganisation: TasksListResponse) -> DbTasks,
    private val mapDbTasksToNetwork:(networkOrganisation: DbTasks) -> TasksListResponse,
    private val mapNetworkScheduleToDbMapper: (networkSchedule: Schedule,taskId:String) -> DbSchedule,
    private val mapDbScheduleToNetwork:(dbSchedule: DbSchedule) -> Schedule
) : HomeRepository {

    override suspend fun loadTasks(): Result<HomeScreenError, List<TasksListResponse>?> =
        withContext(Dispatchers.IO) {
            try {
                    val apiResponse = homeApi.fetchTasks()
                    val tasks = apiResponse.body()
                    if (tasks.isNullOrEmpty()) {
                        return@withContext Result.Fail(HomeScreenError.UnknownError)
                    } else {
                        tasks.forEach { task->
                            task.schedule?.let{
                                scheduleDao.insertOrUpdate(mapNetworkScheduleToDbMapper(it,task._id!!))
                            }
                        }

                        val dbTasks = tasks.map { mapNetworkTasksToDb(it) }

                        tasksDao.insertOrUpdate(*dbTasks.toTypedArray())

                        Result.Ok(tasks)
                    }

            } catch (exception: Exception) {
                exception.printStackTrace()
                if (exception is IOException) {
                    return@withContext Result.Fail(HomeScreenError.NetworkError)
                }

                Result.Fail(HomeScreenError.UnknownError)
            }

        }

    override suspend fun loadTaskFromLocalDB(): Result<HomeScreenError, List<TasksListResponse>?> =
        withContext(Dispatchers.IO) {
            try {
                if(tasksDao.count()==0){
                    return@withContext Result.Fail(HomeScreenError.TaskNotLoaded)
                }
                val list=tasksDao.findAll().map { mapDbTasksToNetwork(it) }
                if(list.isNullOrEmpty()){
                    return@withContext Result.Fail(HomeScreenError.TaskNotLoaded)
                }else{
                    for (i in list.indices){
                        scheduleDao.findByTaskId(list[i]._id!!).let{
                            list[i].schedule=mapDbScheduleToNetwork(it)

                        }
                    }
                }
                Result.Ok(list)
            } catch (exception: Exception) {
                exception.printStackTrace()
                if (exception is IOException) {
                    return@withContext Result.Fail(HomeScreenError.TaskNotLoaded)
                }

                Result.Fail(HomeScreenError.TaskNotLoaded)
            }

        }

    override suspend fun loadTaskByIDFromDb(taskId: String): Result<HomeScreenError, TasksListResponse> =
        withContext(Dispatchers.IO) {
            try {
                if(tasksDao.count()==0){
                    return@withContext Result.Fail(HomeScreenError.TaskNotLoaded)
                }

                val task=mapDbTasksToNetwork(tasksDao.findById(taskId))

                scheduleDao.findByTaskId(taskId).let{
                    task.schedule=mapDbScheduleToNetwork(it)
                }

                Result.Ok(task)
            } catch (exception: Exception) {
                exception.printStackTrace()
                if (exception is IOException) {
                    return@withContext Result.Fail(HomeScreenError.TaskNotLoaded)
                }

                Result.Fail(HomeScreenError.TaskNotLoaded)
            }

        }


}