package com.example.makeMyBudget.daoS

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.makeMyBudget.entities.User

@Dao
interface UserAgent {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM user_diary WHERE user_id = :user_id")
    suspend fun getUser(user_id: String): User?

}