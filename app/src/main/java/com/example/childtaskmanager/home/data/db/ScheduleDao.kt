package com.example.childtaskmanager.home.data.db

import androidx.room.Dao
import androidx.room.Query
import com.example.childtaskmanager.core.data.db.BaseDao
import com.example.childtaskmanager.home.data.dbModel.DbSchedule
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ScheduleDao : BaseDao<DbSchedule>() {

    @Query("SELECT COUNT(*) FROM schedule")
    abstract suspend fun count(): Int


    @Query(value = "SELECT * FROM schedule WHERE id = :id")
    abstract suspend fun findById(id: String): DbSchedule

    @Query(value = "SELECT * FROM schedule WHERE task_id = :taskId LIMIT 1")
    abstract suspend fun findByTaskId(taskId: String): DbSchedule

    @Query(
        value = """
            SELECT * 
            FROM schedule INNER JOIN task_schedule 
            ON schedule.task_id = task_schedule.id 
            WHERE task_schedule.id = :taskId  LIMIT 1 """)
    abstract fun findAllForTask(taskId: String): DbSchedule

}
