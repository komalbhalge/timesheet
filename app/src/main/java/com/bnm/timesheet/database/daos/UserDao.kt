package com.bnm.timesheet.database.daos

import androidx.room.*
import com.bnm.timesheet.database.entities.User

@Dao
interface  UserDao {
    /*@Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM user WHERE full_name LIKE :first AND " +
            "email LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): User

    @Insert
    fun insertAll(vararg users: User)

    @Delete
    fun delete(user: User)*/

    @Insert
    fun insertUser(user: User)

    @Query("Select * from user")
    fun gelAllUsers(): List<User>

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user: User)
}