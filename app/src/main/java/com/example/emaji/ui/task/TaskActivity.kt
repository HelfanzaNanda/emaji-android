package com.example.emaji.ui.task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.emaji.R
import com.example.emaji.models.Task
import com.example.emaji.utils.Constants
import com.example.emaji.utils.ext.gone
import com.example.emaji.utils.ext.showToast
import com.example.emaji.utils.ext.visible
import kotlinx.android.synthetic.main.activity_task.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TaskActivity : AppCompatActivity(), TaskListener {

    private val taskViewModel : TaskViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        setUpRecyclerView()
        observe()
    }

    private fun setUpRecyclerView() {
        recycler_view.apply {
            adapter = TaskAdapter(mutableListOf(),this@TaskActivity)
            layoutManager = LinearLayoutManager(this@TaskActivity)
        }
    }

    private fun observe() {
        observeState()
        observeTasks()
    }

    private fun observeState() = taskViewModel.listenToState().observer(this, Observer { handleUiState(it) })
    private fun observeTasks() = taskViewModel.listenToTasks().observe(this, Observer { handleTasks(it) })
    private fun getPassedToolId() : Int = intent.getIntExtra("TOOL_ID", 0)
    private fun getPassedCycleId() : Int = intent.getIntExtra("CYCLE_ID", 0)
    private fun getTasks() = taskViewModel.getTasks(
        Constants.getToken(this@TaskActivity),
        getPassedCycleId().toString(),
        getPassedToolId().toString()
    )

    private fun handleTasks(list: List<Task>?) {
        list?.let {
            recycler_view.adapter?.let { adapter ->
                if (adapter is TaskAdapter) adapter.updateList(it)
            }
        }
    }

    private fun handleUiState(taskState: TaskState?) {
        taskState?.let {
            when(it){
                is TaskState.Loading -> handleLoading(it.state)
                is TaskState.ShowToast -> showToast(it.m)
                is TaskState.Success -> handleSuccess()
            }
        }
    }

    private fun handleLoading(state: Boolean) {
        if (state) loading.visible() else loading.gone()
    }

    private fun handleSuccess() {
        TODO("Not yet implemented")
    }

    override fun click(task: Task) {
        TODO("Not yet implemented")
    }

    override fun onResume() {
        super.onResume()
        getTasks()
    }
}