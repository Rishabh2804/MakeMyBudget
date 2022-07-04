package com.example.makeMyBudget.mainScreen.viewModels

import android.app.Application
import androidx.lifecycle.*
import com.example.makeMyBudget.entities.Transaction
import com.example.makeMyBudget.entities.TransactionStatus
import com.example.makeMyBudget.repositories.TransactionManagerRepo
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TransactionManagerRepo = TransactionManagerRepo(application)

    private val _transactionID: MutableLiveData<Long> = MutableLiveData<Long>(0L)
    val transactionID: LiveData<Long>
        get() = _transactionID

    private val _userID: MutableLiveData<String> = MutableLiveData<String>("")
    val userID: LiveData<String>
        get() = _userID

    fun setTransId(id: Long) {
        _transactionID.value = id
    }

    fun setUserID(id: String) {
        _userID.value = id
    }

    val transaction: LiveData<Transaction> = Transformations.switchMap(_transactionID) {
        repository.getTransaction(userID.value!!, it)
    }

    fun insertOrUpdate(transaction: Transaction) {
        viewModelScope.launch {
            if (_transactionID.value == 0L) {
                repository.insert(transaction)
            } else {
                repository.update(transaction)
            }
        }
    }

    fun delete(transaction: Transaction) {
        viewModelScope.launch {
            repository.delete(transaction)
        }
    }

    fun complete(transaction: Transaction) {
        val transaction1 = Transaction(
            transaction.user_id,
            transaction.trans_id,
            transaction.title,
            transaction.description,
            transaction.transactionAmount,
            transaction.transactionDate,
            transaction.isRecurring,
            transaction.fromDate,
            transaction.toDate,
            transaction.month,
            transaction.year,
            transaction.monthYear,
            transaction.transactionType,
            transaction.transactionCategory,
            transaction.transactionMode,
            TransactionStatus.COMPLETED,
        )

        insertOrUpdate(transaction1)
    }
}