package com.example.todolist

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row.view.*

class TodoAdapter(private val listTodo:List<TodoData>):RecyclerView.Adapter<TodoAdapter.todoViewHolder>() {


    class todoViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val taskName:TextView=itemView.taskN
        val taskTime:TextView=itemView.taskT
        val box:CheckBox=itemView.checkBox
    }

    //send template layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): todoViewHolder {
        //same
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.row,parent,false)
        return todoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: todoViewHolder, position: Int) {
        val curr=listTodo[position]
        holder.taskName.text=curr.taskName
        holder.taskTime.text=curr.taskTime
        holder.box.isChecked=false
        holder.box.setOnClickListener{
            if(holder.box.isChecked){
                holder.taskTime.alpha=0F
                holder.taskName.paintFlags= holder.taskName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            else {
                holder.taskName.paintFlags=holder.taskName.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                holder.taskTime.alpha=1F
            }
        }
    }

    override fun getItemCount(): Int {
        return listTodo.size
    }
}