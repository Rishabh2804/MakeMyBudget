package com.example.makeMyBudget.daoS

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.makeMyBudget.entities.*
import java.util.*

@Dao
interface ListHandler {

    @Query("SELECT * FROM transactions WHERE transactionDate = :date")
    fun getTransactions(date: Long): LiveData<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE transactionType = :transactionType ORDER BY transactionDate DESC")
    fun getTransactionsByType(transactionType: TransactionType): LiveData<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE transactionCategory = :transactionCategory")
    fun getTransactionsByCategory(transactionCategory: TransactionCategory): LiveData<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE transactionMode = :transactionMode ORDER BY transactionDate DESC")
    fun getTransactionsByMode(transactionMode: TransactionMode): LiveData<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE transactionStatus = :transactionStatus ORDER BY transactionDate DESC")
    fun getTransactionsByStatus(transactionStatus: TransactionStatus): LiveData<List<Transaction>>

    @Query("SELECT SUM(transactionAmount) FROM transactions WHERE transactionCategory = :transactionCategory")
    fun getAmountByCategory(transactionCategory: TransactionCategory): LiveData<Double>

    @Query("SELECT SUM(transactionAmount) FROM transactions WHERE transactionMode = :transactionMode ORDER BY transactionDate DESC")
    fun getAmountByMode(transactionMode: TransactionMode): LiveData<Double>

    @Query("SELECT SUM(transactionAmount) FROM transactions WHERE transactionType = :transactionType")
    fun getAmountByType(transactionType: TransactionType): LiveData<Double>

    @Query("SELECT SUM(transactionAmount) FROM transactions WHERE transactionStatus = :transactionStatus")
    fun getAmountByStatus(transactionStatus: TransactionStatus): LiveData<Double>

    @Query("SELECT SUM(transactionAmount) FROM transactions WHERE transactionDate = :date")
    fun getAmountByDate(date: Long): LiveData<Double>

    @Query("SELECT SUM(transactionAmount) FROM transactions WHERE monthYear = :monthYear")
    fun getAmountByMonth(monthYear: Long): LiveData<Double>

    @Query("SELECT SUM(transactionAmount) FROM transactions WHERE transactionMode = :transactionMode and transactionDate = :date")
    fun getAmountByModeAndDate(transactionMode: TransactionMode, date: Long): LiveData<Double>

    @Query("SELECT SUM(transactionAmount) FROM transactions WHERE transactionMode = :transactionMode and transactionStatus = :status")
    fun getAmountByModeAndStatus(
        transactionMode: TransactionMode,
        status: TransactionStatus
    ): LiveData<Double>

    @Query("SELECT SUM(transactionAmount) FROM transactions WHERE transactionMode = :transactionMode and transactionType = :transactionType")
    fun getAmountByModeAndType(
        transactionMode: TransactionMode,
        transactionType: TransactionType
    ): LiveData<Double>

    @Query("SELECT SUM(transactionAmount) FROM transactions WHERE transactionStatus = :transactionStatus and transactionType = :transactionType")
    fun getAmountByStatusAndType(
        transactionStatus: TransactionStatus,
        transactionType: TransactionType
    ): LiveData<Double>

    @Query("SELECT * FROM transactions WHERE monthYear = :monthYear ORDER BY transactionDate DESC")
    fun getTransactionsByMonth(monthYear: Long): LiveData<List<Transaction>>

    @Query("SELECT * FROM transactions GROUP BY monthYear ORDER BY year, month DESC")
    fun getTransactionsAllMonths(): LiveData<List<Transaction>>

}