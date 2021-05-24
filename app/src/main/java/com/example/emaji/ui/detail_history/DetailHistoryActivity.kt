package com.example.emaji.ui.detail_history

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.emaji.R
import com.example.emaji.models.History
import com.example.emaji.models.HistoryImages
import com.example.emaji.models.HistoryItems
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.activity_detail_history.*

class DetailHistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_history)
        setUpRecyclerView()
        setUpImageSlider()
        setUpData()
    }

    private fun setUpImageSlider() {
        image_slider.apply {
            setSliderAdapter(ImageSliderAdapter(mutableListOf(), this@DetailHistoryActivity))
            setIndicatorAnimation(IndicatorAnimationType.WORM)
            setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
            autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
            indicatorSelectedColor = Color.WHITE
            indicatorUnselectedColor = Color.GRAY
            scrollTimeInSec = 4
        }.startAutoCycle()
    }

    @SuppressLint("SetTextI18n")
    private fun setUpData() {
        getPassedHistory()?.let {
            text_tool_name.text = "Alat : "+it.tool_name
            text_cycle_name.text = "Siklus : "+it.cycle_name
            text_user_name.text = "Penguji : "+it.user_name
            txt_note.text = if (it.note.isNullOrEmpty()) getString(R.string.not_found_note) else it.note
            setDataRecyclerView(it.history_items)
            if (it.history_images.size > 0) setDataImages(it.history_images) else text_images.text = getString(
                            R.string.not_found_image)

        }
    }

    private fun setDataImages(historyImages: MutableList<HistoryImages>) {
        image_slider.sliderAdapter?.let { pagerAdapter ->
            if (pagerAdapter is ImageSliderAdapter) pagerAdapter.changelist(historyImages)
        }
    }

    private fun setDataRecyclerView(historyItems: MutableList<HistoryItems>) {
        recycler_view.adapter?.let { adapter ->
            if (adapter is DefailHistoryAdapter) adapter.updateList(historyItems)
        }
    }

    private fun setUpRecyclerView() {
        recycler_view.apply {
            adapter = DefailHistoryAdapter(mutableListOf(), this@DetailHistoryActivity)
            layoutManager = LinearLayoutManager(this@DetailHistoryActivity)
        }
    }


    private fun getPassedHistory() = intent.getParcelableExtra<History>("HISTORY")
}