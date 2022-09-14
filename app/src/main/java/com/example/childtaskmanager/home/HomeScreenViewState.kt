package com.example.childtaskmanager.home


import TasksListResponse
import com.example.childtaskmanager.core.utils.Constants
import com.example.childtaskmanager.home.domain.error.HomeScreenError

data class HomeScreenViewState(
    val isLoading: Boolean = false,
    val tasks: List<TasksListResponse>?=null,
    val task: TasksListResponse?=null,
    val day: Constants.DayOfTheWeek?=null
    )


sealed class HomeScreenViewEvents {
    data class HomeScreenRequestFailed(val error: HomeScreenError) : HomeScreenViewEvents()
    data class HomeScreenRequestSuccess(val response:Any) : HomeScreenViewEvents()
}


