package com.example.makeMyBudget.mainScreen.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.makeMyBudget.entities.User
import com.example.makeMyBudget.repositories.UserAgentRepo
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UserModel(application: Application) : AndroidViewModel(application) {

    private val _userRepo = UserAgentRepo(application)

    private val _userID: MutableLiveData<String> = MutableLiveData("")

    fun setUserID(user_ID: String) {
        this._userID.value = user_ID
    }

    val user: User?
        get() = runBlocking {
            _userRepo.getUser(_userID.value!!)
        }

    fun insertUser(user: User) {
        viewModelScope.launch {
            _userRepo.insert(user)
        }
    }
}