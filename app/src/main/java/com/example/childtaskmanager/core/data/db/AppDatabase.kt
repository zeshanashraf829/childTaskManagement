package com.example.childtaskmanager.core.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.childtaskmanager.home.data.db.ScheduleDao
import com.example.childtaskmanager.home.data.db.TaskDao
import com.example.childtaskmanager.home.data.db.TaskScheduleDao
import com.example.childtaskmanager.home.data.dbModel.DbSchedule
import com.example.childtaskmanager.home.data.dbModel.DbTaskSchedule
import com.example.childtaskmanager.home.data.dbModel.DbTasks

@Database(
    entities = [
        DbTasks::class,
        DbSchedule::class,
        DbTaskSchedule::class
    ],
    exportSchema = false,
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tasksDao(): TaskDao
    abstract fun scheduleDao(): ScheduleDao
    abstract fun taskScheduleDao(): TaskScheduleDao


    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "apptask.db"
                ).fallbackToDestructiveMigration()
                    .build()
            }

            return INSTANCE as AppDatabase
        }
    }
}