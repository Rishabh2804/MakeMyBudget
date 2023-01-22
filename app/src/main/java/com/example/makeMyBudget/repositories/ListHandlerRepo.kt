package com.example.makeMyBudget.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.makeMyBudget.daoS.BarChartDetails
import com.example.makeMyBudget.daoS.MonthDetail
import com.example.makeMyBudget.daoS.TransactionDB
import com.example.makeMyBudget.entities.*

class ListHandlerRepo(application: Application) {
    private val listHandlerDao = TransactionDB.getDatabase(application).listHandlerDao()

    suspend fun deleteUserData(userId: String) {
        listHandlerDao.deleteUserData(userId)
    }

    fun getAllTransactionsByDate(user_id: String, date: Long): LiveData<List<Transaction>> =
        listHandlerDao.getTransactions(user_id, date)

    fun getAllTransactionsByCategory(
        user_id: String,
        category: TransactionCategory
    ): LiveData<List<Transaction>> =
        listHandlerDao.getTransactionsByCategory(user_id, category)

    fun getAllTransactionsByType(
        user_id: String,
        type: TransactionType
    ): LiveData<List<Transaction>> =
        listHandlerDao.getTransactionsByType(user_id, type)

    fun getAllTransactionsByMode(
        user_id: String,
        mode: TransactionMode
    ): LiveData<List<Transaction>> =
        listHandlerDao.getTransactionsByMode(user_id, mode)

    fun getAllTransactionsByStatus(
        user_id: String,
        status: TransactionStatus
    ): LiveData<List<Transaction>> =
        listHandlerDao.getTransactionsByStatus(user_id, status)

    fun getAmountByCategory(user_id: String, category: TransactionCategory): LiveData<Double> =
        listHandlerDao.getAmountByCategory(user_id, category)

    fun getAmountByCategoryAll(user_id: String): LiveData<Map<TransactionCategory, Double>> =
        listHandlerDao.getAmountByCategoryAll(user_id)

    fun getAmountByType(user_id: String, type: TransactionType): LiveData<Double> =
        listHandlerDao.getAmountByType(user_id, type)

    fun getAmountByTypeAll(user_id: String): LiveData<Map<TransactionType, Double>> =
        listHandlerDao.getAmountByTypeAll(user_id)

    fun getAmountByMode(user_id: String, mode: TransactionMode): LiveData<Double> =
        listHandlerDao.getAmountByMode(user_id, mode)

    fun getAmountByModeAll(user_id: String): LiveData<Map<TransactionMode, Double>> =
        listHandlerDao.getAmountByModeAll(user_id)

    fun getBarChartDetailsByMonth(user_id: String): LiveData<Map<Int, BarChartDetails>> =
        listHandlerDao.getBarChartDetailsByMonth(user_id)

    fun getBarChartDetailsByYear(user_id: String): LiveData<Map<Int, BarChartDetails>> =
        listHandlerDao.getBarChartDetailsByYear(user_id)

    fun getAmountByStatus(user_id: String, status: TransactionStatus): LiveData<Double> =
        listHandlerDao.getAmountByStatus(user_id, status)

    fun getAmountByDate(user_id: String, date: Long): LiveData<Double> =
        listHandlerDao.getAmountByDate(user_id, date)

    fun getAmountByMonth(user_id: String, monthYear: Long): LiveData<Double> =
        listHandlerDao.getAmountByMonth(user_id, monthYear)

    fun getAmountByYear(user_id: String, year: Int): LiveData<Double> =
        listHandlerDao.getAmountByYear(user_id, year)

    fun getAmountByModeAndDate(
        user_id: String,
        mode: TransactionMode,
        date: Long
    ): LiveData<Double> =
        listHandlerDao.getAmountByModeAndDate(user_id, mode, date)

    fun getAmountByStatusAndType(
        user_id: String,
        transactionStatus: TransactionStatus,
        transactionType: TransactionType
    ): LiveData<Double> =
        listHandlerDao.getAmountByStatusAndType(user_id, transactionStatus, transactionType)

    fun getAmountByModeAndStatus(
        user_id: String,
        transactionMode: TransactionMode,
        transactionStatus: TransactionStatus
    ): LiveData<Double> =
        listHandlerDao.getAmountByModeAndStatus(user_id, transactionMode, transactionStatus)

    fun getAmountByModeAndType(
        user_id: String,
        transactionMode: TransactionMode,
        transactionType: TransactionType
    ): LiveData<Double> =
        listHandlerDao.getAmountByModeAndType(user_id, transactionMode, transactionType)

    fun getYears(user_id: String): LiveData<List<Int>> =
        listHandlerDao.getYears(user_id)

    fun getTransactionsByMonthYear(user_id: String, monthYear: Int): LiveData<List<Transaction>> =
        listHandlerDao.getTransactionsByMonth(user_id, monthYear)

    fun getAmountByMonthYearAndType(
        user_id: String,
        transactionType: TransactionType,
        monthYear: Int
    ): LiveData<Double> =
        listHandlerDao.getAmountByMonthYearAndType(user_id, transactionType, monthYear)

    fun getMonthlyTransactionInfo(user_id: String): LiveData<Map<Int, BarChartDetails>> =
        listHandlerDao.getMonthlyTransactionInfo(user_id)

    fun getYearlyTransactionInfo(user_id: String): LiveData<Map<Int, BarChartDetails>> =
        listHandlerDao.getYearlyTransactionInfo(user_id)

    fun getMonthDetailByYear(
        user_id: String
    ): LiveData<Map<Int, List<MonthDetail>>> =
        listHandlerDao.getMonthDetailByYear(user_id)


    fun getAmountByYearAndType(
        user_id: String,
        transactionType: TransactionType,
    ): LiveData<Map<Int,Double>> =
        listHandlerDao.getAmountByYearAndType(user_id, transactionType)

    fun getTransactionDatesByMonthYear(user_id: String, monthYear: Int): LiveData<List<Long>> =
        listHandlerDao.getTransactionDatesByMonthYear(user_id, monthYear)

    fun getTransactionByDate(user_id: String, date: Long): LiveData<List<Transaction>> =
        listHandlerDao.getTransactionByDate(user_id, date)

    fun getTransactionAmountByDateAndType(
        user_id: String,
        date: Long,
        transactionType: TransactionType
    ): LiveData<Double> =
        listHandlerDao.getTransactionAmountByDateAndType(user_id, date, transactionType)

    fun getUpcomingTransactions(user_id: String, date: Long): LiveData<List<Transaction>> =
        listHandlerDao.getUpcomingTransactions(user_id, date)

    fun getPendingTransactions(user_id: String, date: Long): LiveData<List<Transaction>> =
        listHandlerDao.getPendingTransactions(user_id, date)


}