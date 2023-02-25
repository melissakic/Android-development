package com.example.todolist


import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast


class AddTask : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        val closeBtn=findViewById<Button>(R.id.Close)
        closeBtn.setOnClickListener{
            finish()
        }

        val saveBtn=findViewById<Button>(R.id.Save)
        saveBtn.setOnClickListener{
           var data=Intent()
            val name=findViewById<TextView>(R.id.taskName)
            val time=findViewById<TextView>(R.id.taskTime)
            if(checkIfEmpty(name.text.toString()) || checkIfEmpty(time.text.toString())) {
                resetFields(name,time)
                return@setOnClickListener
            }
            data.putExtra("taskName",name.text.toString())
            data.putExtra("taskTime",time.text.toString())
            setResult(12,data)
            finish()
        }
    }

    fun checkIfEmpty(text:String): Boolean {
        if (text=="") return true
        return false
    }
    fun resetFields(name:TextView,time:TextView){
        name.text=""
        time.text=""
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Alert")
        builder.setMessage("Enter valid informations,fields can not be empty")
        builder.setPositiveButton("OK"){dialogInterface, which -> null}
        builder.show()
    }
}