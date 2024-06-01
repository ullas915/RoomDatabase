package com.example.simpleroom

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface Dao {
    @Insert
    fun insert(eData: EData)
    @Query("delete from Test where id=:iD")
    fun delete(iD: String)
    @Query("Select * from Test order by id ASC")
    fun getAll():LiveData<List<EData>>
    //live data helps to reload itself while showing data
    @Query("delete from Test")
    fun deleteAll()
    @Query("Update Test set name=:nAME,number=:nUM where id=:iD")
    fun update(nAME:String,nUM:String,iD:String)

}