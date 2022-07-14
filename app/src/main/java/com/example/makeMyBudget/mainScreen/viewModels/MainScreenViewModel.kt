package com.example.makeMyBudget.mainScreen.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.makeMyBudget.daoS.MonthDetail
import com.example.makeMyBudget.entities.*
import com.example.makeMyBudget.repositories.ListHandlerRepo
import java.util.*
import kotlin.collections.HashMap

class MainScreenViewModel(application: Application) : AndroidViewModel(application) {

    private val listHandlerRepo = ListHandlerRepo(application)
    private val _transactionID: MutableLiveData<Long> = MutableLiveData<Long>(0L)

    private val _userID: MutableLiveData<String> = MutableLiveData<String>("")
    val userID: LiveData<String>
        get() = _userID

    fun setUserID(id: String) {
        _userID.value = id
    }

    val date: MutableLiveData<Long> = MutableLiveData<Long>(0L)
    fun setDate(date: Long) {
        this.date.value = date
    }

    private val monthYear: MutableLiveData<Int> = MutableLiveData(0)
    fun setMonthYear(monthYear: Int) {
        this.monthYear.value = monthYear
    }

    val allTimeGains: LiveData<Double> = Transformations.switchMap(_userID) {
        listHandlerRepo.getAmountByType(it, TransactionType.INCOME)
    }

    val allTimeExpense: LiveData<Double> = Transformations.switchMap(_userID) {
        listHandlerRepo.getAmountByType(it, TransactionType.EXPENSE)
    }

    val choice = MutableLiveData("Category")
    fun setChoice(choice: String) {
        this.choice.value = choice
    }

    val years: LiveData<List<Int>> = Transformations.switchMap(_userID) {
        listHandlerRepo.getYears(it)
    }

    val categoryInfo: MutableLiveData<MutableList<Double>> = MutableLiveData(mutableListOf())
    val typesInfo: MutableLiveData<MutableList<Double>> = MutableLiveData(mutableListOf())
    val transModesInfo: MutableLiveData<MutableList<Double>> = MutableLiveData(mutableListOf())

    fun fetchCategoriesData() {
        TransactionCategory.values().forEach {
            categoryInfo.value?.add(
                listHandlerRepo.getAmountByCategory(
                    _userID.value!!,
                    it
                ).value!!
            )
        }
    }

    fun fetchTypesData() {
        TransactionType.values().forEach {
            typesInfo.value?.add(listHandlerRepo.getAmountByType(_userID.value!!, it).value!!)
        }
    }

    fun fetchModesData() {
        TransactionMode.values().forEach {
            transModesInfo.value?.add(listHandlerRepo.getAmountByMode(_userID.value!!, it).value!!)
        }
    }

    /* <-- Transactions according to Transaction Status -->*/
    fun getCompletedTransactions() =
        listHandlerRepo.getAllTransactionsByStatus(_userID.value!!, TransactionStatus.COMPLETED)

    fun getUpcomingTransactions(date: Long) =
        listHandlerRepo.getUpcomingTransactions(_userID.value!!, date)

    fun getPendingTransactions(date: Long) =
        listHandlerRepo.getPendingTransactions(_userID.value!!, date)

    var getDates : LiveData<List<Long>> = Transformations.switchMap(monthYear) {
        listHandlerRepo.getTransactionDatesByMonthYear(_userID.value!!, it)
    }

    var transactionsByDate = Transformations.switchMap(date) {
        listHandlerRepo.getTransactionByDate(_userID.value!!, it)
    }

    var incomeByDateAndType = Transformations.switchMap(date) {
        listHandlerRepo.getTransactionAmountByDateAndType(
            _userID.value!!,
            it,
            TransactionType.INCOME
        )
    }

    var expenseByDateAndType = Transformations.switchMap(date) {
        listHandlerRepo.getTransactionAmountByDateAndType(
            _userID.value!!,
            it,
            TransactionType.EXPENSE
        )
    }

    val monthlyTransactionsInfo = Transformations.switchMap(_userID) {
        listHandlerRepo.getMonthlyTransactionInfo(_userID.value!!)
    }

    val yearlyTransactionsInfo = Transformations.switchMap(_userID) {
        listHandlerRepo.getYearlyTransactionInfo(_userID.value!!)
    }

    val pieChartCategoryData = Transformations.switchMap(_userID) {
        listHandlerRepo.getAmountByCategoryAll(_userID.value!!)
    }

    val pieChartTypeData = Transformations.switchMap(_userID) {
        listHandlerRepo.getAmountByTypeAll(_userID.value!!)
    }

    val pieChartModeData = Transformations.switchMap(_userID) {
        listHandlerRepo.getAmountByModeAll(_userID.value!!)
    }

    val barChartMonthsInfo = Transformations.switchMap(_userID) {
        listHandlerRepo.getMonthlyTransactionInfo(_userID.value!!)
    }

    val barChartYearsInfo = Transformations.switchMap(_userID) {
        listHandlerRepo.getYearlyTransactionInfo(_userID.value!!)
    }

    val monthlyTransactions: LiveData<List<Transaction>> =
        Transformations.switchMap(monthYear) {
            listHandlerRepo.getTransactionsByMonthYear(userID.value!!, it)
        }

    val monthlyExpenses: LiveData<Double> = Transformations.switchMap(monthYear) {
        listHandlerRepo.getAmountByMonthYearAndType(userID.value!!, TransactionType.EXPENSE, it)
    }

    val monthlyGains: LiveData<Double> = Transformations.switchMap(monthYear) {
        listHandlerRepo.getAmountByMonthYearAndType(userID.value!!, TransactionType.INCOME, it)
    }

    val getYearlyExpenses = Transformations.switchMap(_userID) {
        listHandlerRepo.getAmountByYearAndType(it, TransactionType.EXPENSE)
    }

    val getYearlyGains = Transformations.switchMap(_userID) {
        listHandlerRepo.getAmountByYearAndType(it, TransactionType.INCOME)
    }

    val epoxyDataList: LiveData<Map<Int, List<MonthDetail>>> =
        Transformations.switchMap(userID) {
            listHandlerRepo.getMonthDetailByYear(it)
        }

}