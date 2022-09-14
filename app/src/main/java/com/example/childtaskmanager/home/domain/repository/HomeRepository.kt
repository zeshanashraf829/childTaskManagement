package com.example.childtaskmanager.home.domain.repository

import TasksListResponse
import com.example.childtaskmanager.core.domain.Result
import com.example.childtaskmanager.home.domain.error.HomeScreenError

interface HomeRepository {
    suspend fun loadTasks(): Result<HomeScreenError, List<TasksListResponse>?>
    suspend fun loadTaskFromLocalDB(): Result<HomeScreenError, List<TasksListResponse>?>
    suspend fun loadTaskByIDFromDb(taskId:String): Result<HomeScreenError, TasksListResponse>
}
