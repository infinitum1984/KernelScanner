package com.kernel.scanner.database

import android.provider.UserDictionary
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kernel.scanner.model.Cargo

@Dao
interface CargoDatabaseDao {
    @Insert
    suspend fun insert(cargo: Cargo):Long

    @Update
    suspend fun update(cargo: Cargo)

    @Query("Delete FROM cargo_table")
    suspend fun clear()

    @Query("Delete FROM cargo_table WHERE id=:key")
    suspend fun delete(key:Long)

    @Query("SELECT * FROM cargo_table ORDER BY id DESC")
    fun getAllLists():List<Cargo>

    @Query("SELECT * FROM cargo_table WHERE NOT isChecked ORDER BY id DESC")
    fun getQueueList():List<Cargo>

    @Query("SELECT * FROM cargo_table WHERE isChecked ORDER BY id DESC")
    fun getSavedList():List<Cargo>

    @Query("Select * FROM cargo_table WHERE id==:key")
    suspend fun get(key:Long): Cargo



}
