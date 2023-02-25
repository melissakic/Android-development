package com.example.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_home)
        val lgn=findViewById<Button>(R.id.loginBtn)
        lgn.setOnClickListener{
            val intent=Intent(this,LoginPage::class.java)
            startActivity(intent)
        }
        val sgn=findViewById<Button>(R.id.signinBtn)
        sgn.setOnClickListener{
            var intent=Intent(this,SigninPage::class.java)
            startActivity(intent)
        }
    }
}