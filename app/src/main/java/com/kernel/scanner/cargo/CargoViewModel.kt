package com.kernel.scanner.cargo

import android.widget.VideoView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kernel.scanner.model.Cargo
import com.kernel.scanner.repository.Repository
import kotlinx.coroutines.launch

class CargoViewModel:ViewModel() {
    private val _cargo = MutableLiveData<Cargo>()
    val cargo: LiveData<Cargo> get() = _cargo
    fun loadCargoById(id:Long){
        viewModelScope.launch {
            _cargo.value=Repository.getCargo(id)
        }
    }
}