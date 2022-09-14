package com.example.childtaskmanager.home.data.network

import TasksListResponse
import retrofit2.Response
import retrofit2.http.GET


interface HomeApi {

    @GET(value = "/v1/api/rest-test")
    suspend fun fetchTasks(): Response<List<TasksListResponse>>


}