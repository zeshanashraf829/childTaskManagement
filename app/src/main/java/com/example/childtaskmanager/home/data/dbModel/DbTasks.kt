package com.example.childtaskmanager.home.data.dbModel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class DbTasks(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "name")
    val name: String?=null,
    @ColumnInfo(name = "visualAidUrl")
    val visualAidUrl: String? = null
)
