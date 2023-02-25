package com.example.todolist

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_add_task.*
import kotlinx.android.synthetic.main.row.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import kotlin.collections.ArrayList


open class MainActivity : AppCompatActivity(){
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        setDate(findViewById<TextView>(R.id.WeekDay),findViewById<TextView>(R.id.Date))
        var taskName: String?
        var taskTime:String?
        val lista= ArrayList<TodoData>()
        val tableRow=findViewById<RecyclerView>(R.id.tableRow)
        retrieveFromDB(lista,tableRow)


        val intentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
               if (result.resultCode == 12) {
                   taskName=result.data?.getStringExtra("taskName")
                   taskTime=result.data?.getStringExtra("taskTime")
                   if(!addInDatabase(taskName!!,taskTime!!)) {
                       displayText("Item with that name already exists")
                       return@registerForActivityResult
                   }
                    val row=TodoData(taskName.toString(),taskTime.toString(),false)
                    lista+=row
                   val counter=findViewById<TextView>(R.id.TaskCounter)
                   counter.text= (counter.text.toString().toInt() + 1).toString()
                   tableRow.adapter=TodoAdapter(lista)
                   tableRow.layoutManager=LinearLayoutManager(this)
                    tableRow.setHasFixedSize(true)
              }
            }

        tableRow.apply {
            adapter=TodoAdapter(lista)
            layoutManager=LinearLayoutManager(this@MainActivity)
        }

        val SwipeLeft=object:SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position=viewHolder.position
                deleteFromDB(lista[position].taskName!!)
                lista.removeAt(position)
                tableRow.adapter?.notifyItemRemoved(position)
            }
        }

        val itemTouchHelper=ItemTouchHelper(SwipeLeft)

        itemTouchHelper.attachToRecyclerView(tableRow)

        val addTasks= findViewById<Button>(R.id.AddTask)
        addTasks.setOnClickListener{
            intentLauncher.launch(Intent(this,AddTask::class.java))
        }

    }

    fun deleteFromDB(taskName:String){
        db.collection(auth.currentUser?.email!!).document(taskName).delete()
            .addOnFailureListener {it->
                displayText("Error deleting\nItem may reappear on next login")
            }
    }

    fun retrieveFromDB(lista:ArrayList<TodoData>,tableRow:RecyclerView){
        auth=Firebase.auth
        val email= auth.currentUser?.email
        val docs=db.collection(email!!).orderBy("taskTime").get()
            .addOnSuccessListener { result->
                for(doc in result){
                    val taskName=doc.id
                    val taskTime=doc.data["taskTime"]
                    val row=TodoData(taskName.toString(),taskTime.toString(),false)
                    lista+=row
                    val counter=findViewById<TextView>(R.id.TaskCounter)
                    counter.text= (counter.text.toString().toInt() + 1).toString()
                    tableRow.adapter=TodoAdapter(lista)
                    tableRow.layoutManager=LinearLayoutManager(this)
                    tableRow.setHasFixedSize(true)
                }
            }
    }

   private fun setDate(weekday:TextView, date:TextView) {
        weekday.text = LocalDate.now().getDayOfWeek().name
        val month = LocalDate.now().month.name
        val day = LocalDate.now().dayOfMonth
        date.text = " " + month + " " + day + "th"
    }

    private fun addInDatabase(name:String,time:String):Boolean {
        auth=Firebase.auth
        val email=auth.currentUser?.email.toString()
        //dodati
        db.collection(email).document(name).get().addOnSuccessListener{documents->
                if (documents.exists()) displayText("postoji")
        }
        val task= hashMapOf<String,String>("taskTime" to time,"timeAdded" to timeAdded())
        db.collection(email).document(name)
            .set(task)
            .addOnFailureListener{displayText("Adding to database failed this task will be destoryed on next login")}
        return true
    }

    private fun displayText(text:String){
        Toast.makeText(baseContext, text, Toast.LENGTH_SHORT).show()
    }

    private fun timeAdded():String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS")
        val formatted = current.format(formatter)
        return formatted
    }
}
