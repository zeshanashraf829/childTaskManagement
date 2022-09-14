package com.example.childtaskmanager.home.data.db

import androidx.room.Dao
import androidx.room.Query
import com.example.childtaskmanager.core.data.db.BaseDao
import com.example.childtaskmanager.home.data.dbModel.DbTaskSchedule

@Dao
abstract class TaskScheduleDao : BaseDao<DbTaskSchedule>() {

    @Query(value = "SELECT COUNT(*) FROM task_schedule WHERE taskId =:id")
    abstract suspend fun countForTask(id: String): Int


    @Query(value = "SELECT COUNT(*) FROM task_schedule WHERE id = :id")
    abstract suspend fun countForSchedule(id: String): Int

}