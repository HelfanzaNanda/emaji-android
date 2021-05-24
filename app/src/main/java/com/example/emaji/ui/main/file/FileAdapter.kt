package com.example.emaji.ui.main.file

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.emaji.R
import com.example.emaji.models.File
import kotlinx.android.synthetic.main.list_item_file.view.*

class FileAdapter (private var files : MutableList<File>, private val context: Context)
    : RecyclerView.Adapter<FileAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_file, parent, false))
    }

    override fun getItemCount(): Int = files.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(files[position], context)

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(file: File, context: Context){
            with(itemView){
                text_file.text = file.name
                setOnClickListener {
                    context.startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse(file.filename)))
                }
            }
        }
    }

    fun updateList(c : List<File>){
        files.clear()
        files.addAll(c)
        notifyDataSetChanged()
    }

}