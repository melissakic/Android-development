package com.example.todolist

import Helper.*
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginPage : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_login_page)
        val btn=findViewById<Button>(R.id.goHomeL)
        val signIn=findViewById<Button>(R.id.signIN)
        val email=findViewById<TextView>(R.id.emailLog)
        val password=findViewById<TextView>(R.id.passwordLog)
        val logIn=findViewById<Button>(R.id.signUP)
        val loading=findViewById<ProgressBar>(R.id.LoadingBar)

        btn.setOnClickListener{
            val intent= Intent(this,Home::class.java)
            startActivity(intent)
            finish()
        }

        signIn.setOnClickListener{
            val intent=Intent(this,SigninPage::class.java)
            startActivity(intent)
            finish()
        }

        logIn.setOnClickListener{
            loading.alpha=1F
            auth=Firebase.auth
            if (!checkEmail(email) || password.text=="") {
                displayText("Enter valid info")
                clear(email,password)
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user=auth.currentUser
                        if (user!=null && user.isEmailVerified) {
                            loading.alpha=.0F
                            startActivity(Intent(this,MainActivity::class.java))
                            finish()
                        }else{
                            displayText("User not verified,please verify your email")
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                       displayText("Log in failed")
                        clear(email,password)
                    }
                }
        }
    }

  private fun displayText(text:String){
        Toast.makeText(baseContext, text, Toast.LENGTH_SHORT).show()
    }
}