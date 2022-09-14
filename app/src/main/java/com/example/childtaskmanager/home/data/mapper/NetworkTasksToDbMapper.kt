package com.example.childtaskmanager.home.data.mapper

import TasksListResponse
import com.example.childtaskmanager.home.data.dbModel.DbTasks

class NetworkTasksToDbMapper{
    fun map(input: TasksListResponse): DbTasks {
        return DbTasks(
            id=input._id!!,
            name = input.name,
            visualAidUrl=input.visualAidUrl)
    }

}
