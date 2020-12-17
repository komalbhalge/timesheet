package com.bnm.timesheet.util

import androidx.room.ColumnInfo

class Constants {
    companion object{
        const val KEY_SELECTED_DATE = "selected_date"

        /*Database Name*/
        const val DATABASE_NAME = "my_timesheet"

        /*User Database columns*/
        const val TABLE_USER = "user"
        const val COL_NAME = "full_name"
        const val COL_EMAIL = "email"
        const val COL_PIN = "pin"
        const val COL_TOTAL_HOURS = "working_hours"

        /*Timesheet Database columns*/
        const val TABLE_TIMESHEET = "timesheet"
        const val COL_MONTHYEAR = "month_year"
        const val COL_DATE = "date"
        const val COL_HOURS = "total_hours"
        const val COL_START_TIME = "in_time"
        const val COL_END_TIME = "out_time"
    }

}