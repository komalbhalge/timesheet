package com.bnm.timesheet.database.daos

import androidx.room.Dao
import androidx.room.Query
import com.bnm.timesheet.database.entities.Timesheets

@Dao
interface  TimesheetDao {

    @Query("SELECT * FROM timesheets")
    fun getAll(): List<Timesheets>
}