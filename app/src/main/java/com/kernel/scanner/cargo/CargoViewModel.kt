package com.kernel.scanner.cargo

import android.widget.VideoView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kernel.scanner.digitsOnly
import com.kernel.scanner.model.Cargo
import com.kernel.scanner.model.Seal
import com.kernel.scanner.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CargoViewModel(id: Long):ViewModel() {
    val cargo=Repository.getCargo(id)
    val seals=Repository.getSeals(id)
    private val _savedState=MutableLiveData<Boolean>().apply { value=false }
    val savedState:LiveData<Boolean> get() = _savedState

    private val _sanState=MutableLiveData<Boolean>().apply { value=false }
    val sanState:LiveData<Boolean> get() = _sanState

    private val _textClear=MutableLiveData<Boolean>().apply { value=false }
    val textClear:LiveData<Boolean> get() = _textClear

    fun processInputNumber(text:String){
        _sanState.value=false
        if (text.length>=9){
            if(text.toCharArray()[0].isLetter()){
                if (text.substring(1,text.length-1).digitsOnly()){
                    _savedState.value=true

                    return
                }
            }
        }
        _savedState.value=false
    }

    fun scan(inputStr:String) {
        if (savedState.value!!){

            viewModelScope.launch {
                if (!cargo.value!!.isChecked){
                    cargo.value!!.isChecked=true

                    Repository.updateCargo(cargo.value!!)
                }
                Repository.addSeal(Seal(cargoId = cargo.value!!.id,number = inputStr))
            }
            _sanState.value=false
            _textClear.value=true
        }else{
            _sanState.value=true

        }
    }
}