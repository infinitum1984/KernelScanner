package com.kernel.scanner.saved

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kernel.scanner.model.Cargo
import com.kernel.scanner.repository.Repository
import kotlinx.coroutines.launch

class SavedViewModel : ViewModel() {

    private val _listSaved = MutableLiveData<List<Cargo>>().apply {
        viewModelScope.launch {
            value= Repository.getSavedCargo()
        }
    }
    val listSaved: LiveData<List<Cargo>> = _listSaved
}