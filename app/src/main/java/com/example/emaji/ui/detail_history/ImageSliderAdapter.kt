package com.example.emaji.ui.detail_history

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.api.load
import com.example.emaji.R
import com.example.emaji.models.HistoryImages
import com.smarteist.autoimageslider.SliderViewAdapter
import kotlinx.android.synthetic.main.list_item_image_slider.view.*

class ImageSliderAdapter(private val images : MutableList<HistoryImages>, private val context: Context) :
    SliderViewAdapter<ImageSliderAdapter.SliderAdapterVH>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        return SliderAdapterVH(LayoutInflater.from(parent.context).inflate(R.layout.list_item_image_slider, null))
    }

    override fun getCount(): Int = images.size

    override fun onBindViewHolder(viewHolder: SliderAdapterVH?, position: Int) = viewHolder!!.bind(images[position], context)

    class SliderAdapterVH(itemView: View) : ViewHolder(itemView){
        fun bind(i: HistoryImages, context: Context){
            with(itemView){ image.load(i.filename!!) }
        }
    }

    fun changelist(c : List<HistoryImages>){
        images.clear()
        images.addAll(c)
        notifyDataSetChanged()
    }
}