package com.example.emaji.ui.detail_history


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.emaji.R
import com.example.emaji.models.HistoryItems
import kotlinx.android.synthetic.main.list_item_detail_history.view.*

class DefailHistoryAdapter (private var historyItems : MutableList<HistoryItems>, private var context: Context)
    : RecyclerView.Adapter<DefailHistoryAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return  ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_detail_history, parent, false))
    }

    override fun getItemCount() = historyItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(historyItems[position], context)

    fun updateList(c : List<HistoryItems>){
        historyItems.clear()
        historyItems.addAll(c)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(historyItem: HistoryItems, context: Context){
            with(itemView){
                text_task.text = historyItem.task_item
                if (historyItem.value.equals("Ya")){
                    yes.isChecked = true
                }else{
                    no.isChecked = true
                }

            }
        }
    }
}