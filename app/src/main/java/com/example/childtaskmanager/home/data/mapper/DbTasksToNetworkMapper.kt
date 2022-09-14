package com.example.childtaskmanager.home.data.mapper

import TasksListResponse
import com.example.childtaskmanager.home.data.dbModel.DbTasks


object DbTasksToNetworkMapper {
    fun map(input: DbTasks): TasksListResponse {
        return TasksListResponse(
            _id = input.id,
            name = input.name.orEmpty(),
            visualAidUrl = input.visualAidUrl,
            schedule=null
        )
    }

}
