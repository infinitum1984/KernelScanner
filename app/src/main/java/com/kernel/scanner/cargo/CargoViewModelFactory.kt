package com.kernel.scanner.cargo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CargoViewModelFactory(val id: Long) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CargoViewModel::class.java)){
            return CargoViewModel(id) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }




}