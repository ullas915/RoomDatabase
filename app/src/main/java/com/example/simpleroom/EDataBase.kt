package com.example.simpleroom

import androidx.room.Dao
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [EData::class], version = 1)
abstract class EDataBase : RoomDatabase(){
    abstract fun getDao():com.example.simpleroom.Dao
}