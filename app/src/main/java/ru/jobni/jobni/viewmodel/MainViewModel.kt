package ru.jobni.jobni.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    private val users: MutableLiveData<List<String>> by lazy {
        MutableLiveData<List<String>>().also {
            //loadUsers()
        }
    }

    fun getUsers(): LiveData<List<String>> {
        return users
    }

//    private fun loadUsers(): List<String> {
//        // Do an asynchronous operation to fetch users.
//    }
}