package com.kernel.scanner.queue

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kernel.scanner.model.Cargo
import com.kernel.scanner.repository.Repository
import kotlinx.coroutines.launch

class QueueViewModel : ViewModel() {

    val listQueue: LiveData<List<Cargo>> =Repository.getQueueCargo()

    fun addTest(){
        viewModelScope.launch {
            Repository.addTestQueue()
        }
    }
}