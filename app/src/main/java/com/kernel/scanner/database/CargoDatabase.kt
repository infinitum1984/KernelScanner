package com.kernel.scanner.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kernel.scanner.model.Cargo

@Database(entities = [Cargo::class],version = 1,exportSchema = false)
abstract class CargoDatabase :RoomDatabase(){
    abstract val cargoDatabaseDao:CargoDatabaseDao
    companion object{
        @Volatile
        private var INSTANCE: CargoDatabase?=null
        fun getInstance(context: Context):CargoDatabase{
            synchronized(this){
                var instance= INSTANCE
                if (instance==null){
                    instance= Room.databaseBuilder(context.applicationContext,CargoDatabase::class.java,"kernel_database")
                        .fallbackToDestructiveMigration()
                        .build()
                }
                INSTANCE=instance
                return instance

            }
        }

    }
}
