package com.kernel.scanner.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kernel.scanner.model.Cargo
import com.kernel.scanner.model.Seal
import kotlinx.coroutines.flow.Flow

@Dao
interface CargoDatabaseDao {

    //Cargo
    @Insert
    suspend fun insert(cargo: Cargo):Long

    @Update
    suspend fun updateCargo(cargo: Cargo)

    @Query("SELECT * FROM cargo_table WHERE isChecked ORDER BY id DESC")
    fun getSavedLiveList():LiveData<List<Cargo>>

    @Query("SELECT * FROM cargo_table WHERE NOT isChecked ORDER BY id DESC")
    fun getQueueLiveList():LiveData<List<Cargo>>

    @Query("Select * FROM cargo_table WHERE id==:key")
    fun getLiveCargo(key:Long): LiveData<Cargo>


    //Seal
    @Insert
    fun insertSeal(seal: Seal)

    @Delete
    fun deleteSeal(seal: Seal)

    @Query("SELECT * FROM seal_table WHERE cargoId==:id")
    fun getLiveSeals(id:Long): LiveData<List<Seal>>

}
