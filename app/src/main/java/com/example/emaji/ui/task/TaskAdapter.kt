package com.example.emaji.ui.task

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.emaji.R
import com.example.emaji.models.Task

class TaskAdapter (private var tasks : MutableList<Task>, private var taskListener: TaskListener)
    : RecyclerView.Adapter<TaskAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return  ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_task, parent, false))
    }

    override fun getItemCount() = tasks.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(tasks[position], taskListener)

    fun updateList(c : List<Task>){
        tasks.clear()
        tasks.addAll(c)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(task: Task, taskListener: TaskListener){
            with(itemView){

            }
        }
    }
}