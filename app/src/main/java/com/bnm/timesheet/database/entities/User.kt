package com.bnm.timesheet.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bnm.timesheet.util.Constants.Companion.COL_EMAIL
import com.bnm.timesheet.util.Constants.Companion.COL_NAME
import com.bnm.timesheet.util.Constants.Companion.COL_PIN
import com.bnm.timesheet.util.Constants.Companion.COL_TOTAL_HOURS
import com.bnm.timesheet.util.Constants.Companion.TABLE_USER

@Entity(tableName = TABLE_USER)
data class User(
    @PrimaryKey(autoGenerate = true)var userId: Int? = null,
    @ColumnInfo(name = COL_NAME) val firstName: String?,
    @ColumnInfo(name = COL_EMAIL) val email: String?,
    @ColumnInfo(name = COL_PIN) val pin: String?,
    @ColumnInfo(name = COL_TOTAL_HOURS) val totalHours: String?
)