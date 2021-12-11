package com.kernel.scanner.queue

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kernel.scanner.model.Cargo
import com.kernel.scanner.repository.Repository
import kotlinx.coroutines.launch

class QueueViewModel : ViewModel() {

    private val _listQueue = MutableLiveData<List<Cargo>>().apply {
        viewModelScope.launch {
            value=Repository.getQueueCargo()
        }
    }
    val listQueue: LiveData<List<Cargo>> = _listQueue
}