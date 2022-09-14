package com.example.childtaskmanager.home.data.db

import androidx.room.Dao
import androidx.room.Query
import com.example.childtaskmanager.core.data.db.BaseDao
import com.example.childtaskmanager.home.data.dbModel.DbTasks
import kotlinx.coroutines.flow.Flow

@Dao
abstract class TaskDao : BaseDao<DbTasks>() {

    @Query("SELECT COUNT(*) FROM tasks")
    abstract suspend fun count(): Int

    @Query("SELECT * FROM tasks")
    abstract fun findAll(): List<DbTasks>/*Flow<>*/

    @Query("SELECT * FROM tasks WHERE id = :id")
    abstract suspend fun findById(id: String): DbTasks

    @Query("SELECT * FROM tasks WHERE name LIKE :query")
    abstract suspend fun search(query: String): List<DbTasks>

}