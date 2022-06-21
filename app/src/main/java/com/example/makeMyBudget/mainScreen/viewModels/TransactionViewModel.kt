package com.example.makeMyBudget.mainScreen.viewModels

import android.app.Application
import androidx.lifecycle.*
import com.example.makeMyBudget.entities.Transaction
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

    fun setUserId(id: String) {
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
}