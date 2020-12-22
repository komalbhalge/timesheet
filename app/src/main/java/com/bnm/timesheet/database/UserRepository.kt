package com.bnm.timesheet.database

import android.content.Context
import com.bnm.timesheet.database.daos.UserDao
import com.bnm.timesheet.database.entities.User

class UserRepository(context: Context) {

    /*var db: UserDao = AppDatabase.getInstance(context)?.userDao()!!


    //Fetch All the Users
    fun getAllUsers(): List<User> {
        return db.gelAllUsers()
    }

    // Insert new user
    fun insertUser(users: User) {
        insertAsyncTask(db).execute(users)
    }

    // update user
    fun updateUser(users: User) {
        db.updateUser(users)
    }

    // Delete user
    fun deleteUser(users: Users) {
        db.deleteUser(users)
    }

    private class insertAsyncTask internal constructor(private val usersDao: UserDao) :
        AsyncTask<Users, Void, Void>() {

        override fun doInBackground(vararg params: Users): Void? {
            usersDao.insertUser(params[0])
            return null
        }
    } */
}
