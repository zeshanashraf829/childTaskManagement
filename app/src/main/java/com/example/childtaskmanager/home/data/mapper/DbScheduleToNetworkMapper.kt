package com.example.childtaskmanager.home.data.mapper

import Schedule
import TasksListResponse
import com.example.childtaskmanager.home.data.dbModel.DbSchedule
import com.example.childtaskmanager.home.data.dbModel.DbTasks

class DbScheduleToNetworkMapper{
    fun map(input: DbSchedule): Schedule {
        return Schedule(
            mon = input.mon,
            tue = input.tue,
            wed = input.wed,
            thu = input.thu,
            fri = input.fri,
            sat = input.sat,
            sun = input.sun
        )
    }

}
