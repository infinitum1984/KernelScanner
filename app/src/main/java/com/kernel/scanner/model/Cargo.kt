package com.kernel.scanner.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cargo_table")

data class Cargo(

    @PrimaryKey(autoGenerate = true)
    val id:Long=0L,

    @ColumnInfo(name = "carNumber")
    val carNumber:String="",

    @ColumnInfo(name = "trailerNumber")
    val trailerNumber:String="",

    @ColumnInfo(name = "driverName")
    val driverName:String="",

    @ColumnInfo(name = "driverPhone")
    val driverPhone:String="",

    @ColumnInfo(name = "sealNumber")
    val sealNumber:String="",

    @ColumnInfo(name = "isChecked")
    var isChecked:Boolean=false
    ){
}