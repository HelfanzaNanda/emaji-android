package com.example.emaji.ui.cycle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.emaji.R
import com.example.emaji.models.Cycle
import com.example.emaji.ui.task.TaskActivity
import com.example.emaji.utils.Constants
import com.example.emaji.utils.ext.gone
import com.example.emaji.utils.ext.showToast
import com.example.emaji.utils.ext.visible
import kotlinx.android.synthetic.main.activity_cycle.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CycleActivity : AppCompatActivity(), CycleListener {

    private val cycleViewModel : CycleViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cycle)
        setUpRecyclerView()
        observe()
    }

    private fun setUpRecyclerView() {
        recycler_view.apply {
            adapter = CycleAdapter(mutableListOf(), this@CycleActivity)
            layoutManager = LinearLayoutManager(this@CycleActivity)
        }
    }

    private fun observe() {
        observeState()
        observeCycles()
    }

    private fun observeState() = cycleViewModel.listenToState().observer(this, Observer { handleUiState(it) })
    private fun observeCycles() = cycleViewModel.listenToCycles().observe(this, Observer { handleCycles(it) })
    private fun getPassedToolId() : Int = intent.getIntExtra("TOOL_ID", 0)
    private fun getCycles() = cycleViewModel.getCycles(Constants.getToken(this@CycleActivity), getPassedToolId())

    private fun handleCycles(list: List<Cycle>?) {
        list?.let {
            recycler_view.adapter?.let { adapter ->
                if (adapter is CycleAdapter) adapter.updateList(it)
            }
        }
    }

    private fun handleUiState(cycleState: CycleState?) {
        cycleState?.let {
            when(it){
                is CycleState.Loading -> handleLoading(it.state)
                is CycleState.ShowToast -> showToast(it.message)
            }
        }
    }

    private fun handleLoading(state: Boolean) {
        if (state) loading.visible() else loading.gone()
    }



    override fun click(cycle: Cycle) {
        startActivity(Intent(this, TaskActivity::class.java).apply {
            putExtra("TOOL_ID", getPassedToolId())
            putExtra("CYCLE_ID", cycle.id)
        })
    }

    override fun onResume() {
        super.onResume()
        getCycles()
    }
}