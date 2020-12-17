package com.bnm.timesheet.database

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bnm.timesheet.database.daos.TimesheetDao
import com.bnm.timesheet.database.daos.UserDao
import com.bnm.timesheet.database.entities.Timesheets
import com.bnm.timesheet.database.entities.User
import com.bnm.timesheet.util.Constants

@Database(entities = arrayOf(User::class, Timesheets::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun timesheetDao(): TimesheetDao

    companion object {
        var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    if (INSTANCE == null) {
                        // Create database here
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext, AppDatabase::class.java,
                            Constants.DATABASE_NAME
                        )
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build()
                    }
                }
            }
            return this.INSTANCE!!
        }

        private val sRoomDatabaseCallback = object : RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                PopulateDbAsync(INSTANCE!!).execute()
            }

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->

                }
            }
        }
    }
    class PopulateDbAsync internal constructor(db: AppDatabase) : AsyncTask<Void, Void, Void>() {
        private val mDao = db.userDao()
        override fun doInBackground(vararg params: Void): Void? {
            // If we have no words, then create the initial list of words
            if (mDao.getAll().isEmpty()) {
                for (i in 0 until words.size) {
                    mDao.insertAll(User(1, "Komal Bhalge", "tomble.808@gmail.com","0909","09"))
                }
            }
            return null
        }
        companion object {
            // Initial data set
            private val words = arrayOf("KD", "Mittal", "Dhurv", "Wellcome", "Chotu", "tiger", "Harsh")
        }
    }

}