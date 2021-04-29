package com.example.emaji.ui.task

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.emaji.R
import com.example.emaji.models.Task
import com.example.emaji.models.TaskItems
import kotlinx.android.synthetic.main.list_item_task.view.*

class TaskAdapter (private var taskItems : MutableList<TaskItems>, private var taskViewModel: TaskViewModel)
    : RecyclerView.Adapter<TaskAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return  ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_task, parent, false))
    }

    override fun getItemCount() = taskItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(taskItems[position], taskViewModel)

    fun updateList(c : List<TaskItems>){
        taskItems.clear()
        taskItems.addAll(c)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(taskItems: TaskItems, taskViewModel: TaskViewModel){
            with(itemView){
                text_task.text = taskItems.body
                answer.setOnCheckedChangeListener { _, isChecked ->
                    taskViewModel.checkedSelectedTask(taskItems, isChecked)
                }
            }
        }
    }
}