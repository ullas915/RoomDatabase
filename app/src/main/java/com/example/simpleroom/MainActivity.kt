package com.example.simpleroom

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.Room
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch





class MainActivity : AppCompatActivity() {


    //number of record counter
    var rowCounter:Int=-1
    //initial zero records,so index -1

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        var name=findViewById<EditText>(R.id.name)
        var num=findViewById<EditText>(R.id.number)
        var idnum=findViewById<EditText>(R.id.id)
        var idToDelete=findViewById<EditText>(R.id.idToDelete)
       var displayData=findViewById<TextView>(R.id.show)
        val insertBtn=findViewById<Button>(R.id.insert)
        val updateBtn=findViewById<Button>(R.id.update)
        val deleteBtn=findViewById<Button>(R.id.delete)
        val dataBtn=findViewById<Button>(R.id.showData)
        val clearAllBtn=findViewById<Button>(R.id.clearAll)
        val clearListBtn=findViewById<Button>(R.id.clearCache)



        //initialize database class
        lateinit var database:EDataBase

        database= Room.databaseBuilder(applicationContext,
            EDataBase::class.java,
            "Test").build()

        //loading the rowCounter value from the sharedPreference
        val sharedPreferences: SharedPreferences =getSharedPreferences("share", Context.MODE_PRIVATE)
        rowCounter=sharedPreferences.getInt("num", 0)





        // To insert
        insertBtn.setOnClickListener {
            val iD=idnum.text.toString()
            val nAME=name.text.toString()
            val nUM=num.text.toString()
            if(nUM.isNotEmpty() && nAME.isNotEmpty() && iD.isNotEmpty()) {
                GlobalScope.launch {
                    database.getDao().insert(EData(iD, nAME, nUM))
                    rowCounter++

                    //storing rowCounter to sharedPreference
                    put(rowCounter)
                }
                Toast.makeText(this, "Inserted", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Enter details", Toast.LENGTH_SHORT).show()
            }
        }



        //To delete
        deleteBtn.setOnClickListener {
            val iD=idToDelete.text.toString()
            if(iD.isNotEmpty()) {
                GlobalScope.launch {
                    database.getDao().delete(iD)
                    rowCounter--

                    //storing rowCounter to sharedPreference
                    put(rowCounter)
                }
                Toast.makeText(this, "Removed", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Enter ID", Toast.LENGTH_SHORT).show()
            }
        }





        //To show
        val mylist = findViewById<ListView>(R.id.listView)

        dataBtn.setOnClickListener {
                database.getDao().getAll().observe(this,{
                    //get rowCounter from sharedPreference
                    val sharedPreferences: SharedPreferences =getSharedPreferences("share", Context.MODE_PRIVATE)
                    rowCounter=sharedPreferences.getInt("num", 0)


                    val mList = mutableListOf<String>()
                    for (i in 0..rowCounter) {
                        mList.add("ID=${it[i].id} and Name=${it[i].name} and Email=${it[i].number}")
                    }
                    val myAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mList)
                    mylist.adapter = myAdapter

                    //displayData.setText("${it}")
                })

            Toast.makeText(this,"Display",Toast.LENGTH_SHORT).show()
        }






        //to clear list
        clearListBtn.setOnClickListener {
            val myList=findViewById<ListView>(R.id.listView)
            val removeList = mutableListOf<String>()
            val myAdapter= ArrayAdapter(this,android.R.layout.simple_list_item_1,removeList)
            myList.adapter=myAdapter
            Toast.makeText(this,"cleared",Toast.LENGTH_SHORT).show()

            displayData.setText(" ")
        }






        //to delete all
        clearAllBtn.setOnClickListener {
            GlobalScope.launch {
                 database.getDao().deleteAll()
                rowCounter=-1
                //storing rowCounter to sharedPreference
                put(rowCounter)

            }
            Toast.makeText(this,"Removed",Toast.LENGTH_SHORT).show()
        }






        //to update
        updateBtn.setOnClickListener {
            val iD=idnum.text.toString()
            val nAME=name.text.toString()
            val nUM=num.text.toString()
            if(nUM.isNotEmpty() && nAME.isNotEmpty() && iD.isNotEmpty()) {
                GlobalScope.launch {
                    database.getDao().update(nAME, nUM, iD)
                }
                Toast.makeText(this, "updated", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Enter details", Toast.LENGTH_SHORT).show()
            }
        }









        //to insert
//        GlobalScope.launch {
//            database.getDao().insert(EData(0,"hg","yf"))
//        }
//        to get
//        database.getDao().getAll().observe(this,{
//            Toast.makeText(this,"This is ${it}",Toast.LENGTH_SHORT).show()
//        })

        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    //SharedPreference
    //sharedPreference is used to store the number of records that are inserted, used in for loop to print the list
    //after closing the application it stores the rowCounter value
    private fun put(rowCounter:Int) {
        val sharedPreferences: SharedPreferences =getSharedPreferences("share", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor? =sharedPreferences.edit()
        editor?.apply {
            putInt("num",rowCounter)

        }?.apply()
    }
}