package com.example.emaji.ui.main.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.emaji.R
import com.example.emaji.models.Tool
import com.example.emaji.utils.ext.showToast
import kotlinx.android.synthetic.main.list_item_home.view.*

class HomeAdapter(private var tools : MutableList<Tool>, private var homeListener: HomeListener)
    : RecyclerView.Adapter<HomeAdapter.ViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_home, parent, false))
    }

    override fun getItemCount() = tools.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(tools[position], homeListener)

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(tool: Tool, homeListener: HomeListener){
            with(itemView){
                //image_tool.load(tool.image)
                name_tool.text = tool.name
                setOnClickListener {
                    homeListener.click(tool)
                }
            }
        }
    }

    fun updateList(c : List<Tool>){
        tools.clear()
        tools.addAll(c)
        notifyDataSetChanged()
    }
}