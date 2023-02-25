package com.example.todolist


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import Helper.*

class SigninPage : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_signin_page)

        val goHome=findViewById<Button>(R.id.goHomeS)
        val logIN=findViewById<Button>(R.id.logIN)
        val email = findViewById<TextView>(R.id.emailLog)
        val password = findViewById<TextView>(R.id.passwordLog)
        val box = findViewById<CheckBox>(R.id.licenseBox)
        auth = Firebase.auth

        goHome.setOnClickListener{
            val intent=Intent(this,Home::class.java)
            startActivity(intent)
            finish()
        }

        logIN.setOnClickListener{
            val intent= Intent(this,LoginPage::class.java)
            startActivity(intent)
            finish()
        }

        val sigUP=findViewById<Button>(R.id.signUP)
        sigUP.setOnClickListener {
            if (checkEmailAndPass(email,password) && !box.isChecked) {
                displayText("Accept license and agreement to proceed")
                return@setOnClickListener
            }
            if (checkEmailAndPass(email, password) && box.isChecked) {
            auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        displayText("User created")
                        val user=auth.currentUser
                        if (user!=null) {
                            user.sendEmailVerification()
                            startActivity(Intent(this,Home::class.java))
                            finish()
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        displayText("Account creation failed")
                        clear(email,password)
                        box.isChecked = false
                    }
                }
        }   else {
                displayText("Enter valid information")
                clear(email,password)
                box.isChecked=false
                return@setOnClickListener
        }
        }
    }

    private fun displayText(text:String){
        Toast.makeText(baseContext, text, Toast.LENGTH_SHORT).show()
    }
}