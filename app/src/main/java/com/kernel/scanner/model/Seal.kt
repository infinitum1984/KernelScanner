package com.kernel.scanner.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "seal_table")

//Пломбп
data class Seal(

    @PrimaryKey(autoGenerate = true)
    val id:Long=0L,

    @ColumnInfo(name = "cargoId")
    val cargoId:Long=0L,

    @ColumnInfo(name = "number")
    val number:String=""

) {
}