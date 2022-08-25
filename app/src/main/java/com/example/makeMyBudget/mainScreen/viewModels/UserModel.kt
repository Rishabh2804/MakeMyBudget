package com.example.makeMyBudget.mainScreen.viewModels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.example.makeMyBudget.entities.User
import com.example.makeMyBudget.repositories.UserAgentRepo
import kotlinx.coroutines.launch

class UserModel(application: Application) : AndroidViewModel(application) {

    private val _userRepo = UserAgentRepo(application)

    private val _userID: MutableLiveData<String> = MutableLiveData("")

    fun setUserID(user_ID: String) {
        this._userID.value = user_ID
    }

    val user: LiveData<User> = Transformations.switchMap(_userID) {
        _userRepo.getUser(it)
    }

    fun insertUser(user: User) {
        viewModelScope.launch {
            _userRepo.insert(user)
        }
    }
}