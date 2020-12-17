package com.bnm.timesheet.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bnm.timesheet.util.Constants


@Entity(tableName = Constants.TABLE_TIMESHEET)
data class Timesheets (
    @PrimaryKey val uid: Int,

    @ColumnInfo(name = Constants.COL_MONTHYEAR) val firstName: String?,
    @ColumnInfo(name = Constants.COL_EMAIL) val email: String?,
    @ColumnInfo(name = Constants.COL_DATE) val date: String?,
    @ColumnInfo(name = Constants.COL_TOTAL_HOURS) val totalHours: String?,
    @ColumnInfo(name = Constants.COL_START_TIME) val startHours: String?,
    @ColumnInfo(name = Constants.COL_END_TIME) val endHours: String?,
)