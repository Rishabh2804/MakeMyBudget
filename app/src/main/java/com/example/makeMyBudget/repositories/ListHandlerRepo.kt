package com.example.makeMyBudget.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.makeMyBudget.daoS.TransactionDB
import com.example.makeMyBudget.entities.*
import java.time.YearMonth
import java.util.*

class ListHandlerRepo(application: Application) {
    private val listHandlerDao = TransactionDB.getDatabase(application).listHandlerDao()

    fun getAllTransactionsByDate(date: Long): LiveData<List<Transaction>> =
        listHandlerDao.getTransactions(date)

    fun getAllTransactionsByCategory(category: TransactionCategory): LiveData<List<Transaction>> =
        listHandlerDao.getTransactionsByCategory(category)

    fun getAllTransactionsByType(type: TransactionType): LiveData<List<Transaction>> =
        listHandlerDao.getTransactionsByType(type)

    fun getAllTransactionsByMode(mode: TransactionMode): LiveData<List<Transaction>> =
        listHandlerDao.getTransactionsByMode(mode)

    fun getAllTransactionsByStatus(status: TransactionStatus): LiveData<List<Transaction>> =
        listHandlerDao.getTransactionsByStatus(status)

    fun getAmountByCategory(category: TransactionCategory): LiveData<Double> =
        listHandlerDao.getAmountByCategory(category)

    fun getAmountByType(type: TransactionType): LiveData<Double> =
        listHandlerDao.getAmountByType(type)

    fun getAmountByMode(mode: TransactionMode): LiveData<Double> =
        listHandlerDao.getAmountByMode(mode)

    fun getAmountByStatus(status: TransactionStatus): LiveData<Double> =
        listHandlerDao.getAmountByStatus(status)

    fun getAmountByDate(date: Long): LiveData<Double> =
        listHandlerDao.getAmountByDate(date)

    fun getAmountByMonth(monthYear: Long): LiveData<Double> =
        listHandlerDao.getAmountByMonth(monthYear)

    fun getAmountByModeAndDate(mode: TransactionMode, date: Long): LiveData<Double> =
        listHandlerDao.getAmountByModeAndDate(mode, date)

    fun getAmountByStatusAndType(
        transactionStatus: TransactionStatus,
        transactionType: TransactionType
    ): LiveData<Double> =
        listHandlerDao.getAmountByStatusAndType(transactionStatus, transactionType)

    fun getAmountByModeAndStatus(
        transactionMode: TransactionMode,
        transactionStatus: TransactionStatus
    ): LiveData<Double> =
        listHandlerDao.getAmountByModeAndStatus(transactionMode, transactionStatus)

    fun getAmountByModeAndType(
        transactionMode: TransactionMode,
        transactionType: TransactionType
    ): LiveData<Double> =
        listHandlerDao.getAmountByModeAndType(transactionMode, transactionType)

    fun getTransactionsByMonthYear(monthYear: Long): LiveData<List<Transaction>> =
        listHandlerDao.getTransactionsByMonth(monthYear)

    fun getTransactionsByMonth(): LiveData<List<Transaction>> =
        listHandlerDao.getTransactionsAllMonths()

}