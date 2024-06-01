package com.example.simpleroom

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Test")
data class EData(
   @PrimaryKey(autoGenerate = false)
    val id:String,
    val name:String,
    val number:String,

)
