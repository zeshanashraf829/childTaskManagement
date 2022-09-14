package com.example.childtaskmanager.home.data.dbModel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "task_schedule",
    primaryKeys = ["id", "taskId"],
    foreignKeys = [
        ForeignKey(
            entity = DbTasks::class,
            parentColumns = ["id"],
            childColumns = ["id"]
        ),
        ForeignKey(
            entity = DbSchedule::class,
            parentColumns = ["id"],
            childColumns = ["taskId"]
        )
    ],
    indices = [
        Index("taskId")
    ]
)
data class DbTaskSchedule(

    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "taskId")
    val taskId: String
)