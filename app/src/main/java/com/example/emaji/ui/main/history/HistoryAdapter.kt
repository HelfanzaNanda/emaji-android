package com.example.emaji.ui.main.history

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.emaji.R
import com.example.emaji.models.History
import com.example.emaji.ui.detail_history.DetailHistoryActivity
import com.example.emaji.utils.ext.showToast
import kotlinx.android.synthetic.main.list_item_history.view.*

class HistoryAdapter (private var histories : MutableList<History>, private var context: Context)
    : RecyclerView.Adapter<HistoryAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_history, parent, false))
    }

    override fun getItemCount() = histories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(histories[position], context)

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(history: History, context: Context){
            with(itemView){
                image_tool.load(history.tool_image)
                name_tool.text = history.tool_name
                setOnClickListener {
                    context.startActivity(Intent(context, DetailHistoryActivity::class.java).apply {
                        putExtra("HISTORY", history)
                    })
                }
            }
        }
    }

    fun updateList(c : List<History>){
        histories.clear()
        histories.addAll(c)
        notifyDataSetChanged()
    }
}