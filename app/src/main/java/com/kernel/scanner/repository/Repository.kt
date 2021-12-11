package com.kernel.scanner.repository

import com.kernel.scanner.KernelApplication
import com.kernel.scanner.database.CargoDatabase
import com.kernel.scanner.model.Cargo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object Repository {
    suspend fun getQueueCargo():List<Cargo> =
        withContext(Dispatchers.IO){
            val outList= listOf<Cargo>()

            if (KernelApplication.getContext()==null) return@withContext outList

            val dataSource=CargoDatabase.getInstance(KernelApplication.getContext()!!).cargoDatabaseDao

            dataSource.getQueueList()

        }
    suspend fun getSavedCargo():List<Cargo> =
        withContext(Dispatchers.IO){
            val outList= listOf<Cargo>()

            if (KernelApplication.getContext()==null) return@withContext outList

            val dataSource=CargoDatabase.getInstance(KernelApplication.getContext()!!).cargoDatabaseDao

            dataSource.getSavedList()

        }
    suspend fun getCargo(id:Long):Cargo = withContext(Dispatchers.IO){
        if (KernelApplication.getContext()==null) return@withContext Cargo()

        val dataSource=CargoDatabase.getInstance(KernelApplication.getContext()!!).cargoDatabaseDao

        dataSource.get(id)

    }

    suspend fun insertTestData(){
        val list= listOf<Cargo>(
            Cargo(carNumber = "AN21323SD", trailerNumber = "AS213233SD",
                driverName = "Иван Иванов Иванович",driverPhone = "0500642665"),

            Cargo(carNumber = "AN23SD", trailerNumber = "AS213233SD",
                driverName = "Петр Иванов Иванович",driverPhone = "0500642665"),
            Cargo(carNumber = "AS21323SD", trailerNumber = "AS213233SD",
                driverName = "Иван Иванов Иванович",driverPhone = "0500642665",isChecked = true, sealNumber = "A12345678"),

            Cargo(carNumber = "AS23SD", trailerNumber = "AS213233SD",
                driverName = "Петр Иванов Иванович",driverPhone = "0500642665",isChecked = true,sealNumber = "A12345678"),
        )
        val dataSource=CargoDatabase.getInstance(KernelApplication.getContext()!!).cargoDatabaseDao

        for (c in list){
            dataSource.insert(c)
        }
    }

}