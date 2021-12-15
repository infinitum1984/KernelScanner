package com.kernel.scanner.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.kernel.scanner.KernelApplication
import com.kernel.scanner.database.CargoDatabase
import com.kernel.scanner.model.Cargo
import com.kernel.scanner.model.Seal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext

object Repository {
    fun getQueueCargo():LiveData<List<Cargo>> {
        val outList=MutableLiveData<List<Cargo>>()

        if (KernelApplication.getContext()==null) return outList

        val dataSource=CargoDatabase.getInstance(KernelApplication.getContext()!!).cargoDatabaseDao

        return dataSource.getQueueLiveList()

    }


     fun getSavedCargo():LiveData<List<Cargo>> {
            if (KernelApplication.getContext()==null) return liveData { }

            val dataSource=CargoDatabase.getInstance(KernelApplication.getContext()!!).cargoDatabaseDao

            return dataSource.getSavedList()

        }
     fun getCargo(id:Long): LiveData<Cargo> {
        if (KernelApplication.getContext()==null) return liveData {  }

        val dataSource=CargoDatabase.getInstance(KernelApplication.getContext()!!).cargoDatabaseDao


        return dataSource.getLiveCargo(id)

    }

    suspend fun addSeal(seal: Seal){
        if (KernelApplication.getContext()==null) return

        withContext(Dispatchers.IO){
            val dataSource=CargoDatabase.getInstance(KernelApplication.getContext()!!).cargoDatabaseDao

            dataSource.insertSeal(seal)
        }

    }
    suspend fun updateCargo(cargo: Cargo){
        if (KernelApplication.getContext()==null) return

        withContext(Dispatchers.IO){
            val dataSource=CargoDatabase.getInstance(KernelApplication.getContext()!!).cargoDatabaseDao

            dataSource.updateCargo(cargo)
        }
    }
    fun getSeals(cargoId:Long):LiveData<List<Seal>>{
        if (KernelApplication.getContext()==null) return liveData {  }

        val dataSource=CargoDatabase.getInstance(KernelApplication.getContext()!!).cargoDatabaseDao

        return dataSource.getSeals(cargoId)
    }

    suspend fun addTestQueue() {
        val dataSource=CargoDatabase.getInstance(KernelApplication.getContext()!!).cargoDatabaseDao
        dataSource.insert(Cargo(carNumber = "AN21323SD", trailerNumber = "AS213233SD",
            driverName = "Иван Иванов Иванович",driverPhone = "0500642665"))
    }

    suspend fun deleteSeal(seal: Seal) {
        if (KernelApplication.getContext()==null) return

        withContext(Dispatchers.IO){
            val dataSource=CargoDatabase.getInstance(KernelApplication.getContext()!!).cargoDatabaseDao
            dataSource.deleteSeal(seal)
        }

    }

}