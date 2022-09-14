package com.example.childtaskmanager.home.data.dbModel

import androidx.room.*

@Entity(tableName = "schedule",
        indices = [androidx.room.Index("task_id", unique = true)]
)
data class DbSchedule(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int?,

    @ColumnInfo(name = "task_id")
    val taskId: String?=null,
    @ColumnInfo(name = "mon")
    val mon: String?=null,
    @ColumnInfo(name = "tue")
    val tue: String? = null,
    @ColumnInfo(name = "wed")
    val wed: String? = null,
    @ColumnInfo(name = "thu")
    val thu: String? = null,
    @ColumnInfo(name = "fri")
    val fri: String? = null,
    @ColumnInfo(name = "sat")
    val sat: String? = null,
    @ColumnInfo(name = "sun")
    val sun: String? = null
)