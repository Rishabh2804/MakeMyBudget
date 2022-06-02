package com.example.makeMyBudget.daoS

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.makeMyBudget.entities.Transaction

@Dao
interface TransactionManager {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: Transaction)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(transaction: Transaction)

    @Delete
    suspend fun delete(transaction: Transaction)

    @Query("SELECT * FROM transactions WHERE user_id = :user_id and trans_id =:trans_id")
    fun getInfo(user_id: String, trans_id: Long): LiveData<Transaction>

}