package com.example.emaji.ui.cycle

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.emaji.R
import com.example.emaji.models.Cycle
import kotlinx.android.synthetic.main.list_item_cycle.view.*

class CycleAdapter (private var cycles : MutableList<Cycle>, private var cycleListener: CycleListener) :
        RecyclerView.Adapter<CycleAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_cycle, parent, false))
    }

    override fun getItemCount() = cycles.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(cycles[position], cycleListener)

    fun updateList(c : List<Cycle>){
       cycles.clear()
        cycles.addAll(c)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(cycle: Cycle, cycleListener: CycleListener){
            with(itemView){
                text_cycle.text = cycle.name
                setOnClickListener {
                    cycleListener.click(cycle)
                }
            }
        }
    }

}