package com.example.emaji.ui.task

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.emaji.R
import com.example.emaji.models.Task
import com.example.emaji.models.TaskItems
import com.example.emaji.models.TaskResult
import com.example.emaji.ui.main.MainActivity
import com.example.emaji.utils.Constants
import com.example.emaji.utils.ext.AlertInfo
import com.example.emaji.utils.ext.gone
import com.example.emaji.utils.ext.showToast
import com.example.emaji.utils.ext.visible
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_task.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TaskActivity : AppCompatActivity() {

    private val taskViewModel : TaskViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        setUpRecyclerView()
        observe()
        sendAnswer()
    }

    private fun setUpRecyclerView() {
        recycler_view.apply {
            adapter = TaskAdapter(mutableListOf(),taskViewModel)
            layoutManager = LinearLayoutManager(this@TaskActivity)
        }
    }

    private fun observe() {
        observeState()
        observeTask()
    }

    private fun observeState() = taskViewModel.listenToState().observer(this, Observer { handleUiState(it) })
    private fun observeTask() = taskViewModel.listenToTask().observe(this, Observer { handleTask(it) })


    private fun sendAnswer(){
        btn_submit_tasks.setOnClickListener {
            val answertasks = taskViewModel.listenToAnswerTasks().value
            if (answertasks.isNullOrEmpty()){
                AlertInfo("harus di kerjakan semua")
            }else{
                 if (taskViewModel.validated(answertasks)){
                     val taskResult = TaskResult(
                             tool_id = getPassedToolId(),
                             cycle_id = getPassedCycleId(),
                             task_id = getTasksId(),
                             tasks = answertasks
                     )
                     taskViewModel.sendAnswer(Constants.getToken(this@TaskActivity), taskResult)
                 }
            }
        }
    }





    @SuppressLint("SetTextI18n")
    private fun handleTask(task: Task?) {
        task?.let {
            taskViewModel.setTaskId(it.id!!)
            text_tools_used.text = it.tools_used
            text_title.text = getString(R.string.text_title_task) + it.cycle_name
            text_sub_title.text = it.tool_name
            recycler_view.adapter?.let { adapter ->
                if (adapter is TaskAdapter) adapter.updateList(it.tasks)
            }
        }
    }

    private fun getTasksId() = taskViewModel.gettaskId().value
    private fun getPassedToolId() : Int = intent.getIntExtra("TOOL_ID", 0)
    private fun getPassedCycleId() : Int = intent.getIntExtra("CYCLE_ID", 0)
    private fun getTasks() = taskViewModel.getTasks(
        Constants.getToken(this@TaskActivity),
        getPassedCycleId().toString(),
        getPassedToolId().toString()
    )

    private fun handleUiState(taskState: TaskState?) {
        taskState?.let {
            when(it){
                is TaskState.Loading -> handleLoading(it.state)
                is TaskState.ShowToast -> showToast(it.m)
                is TaskState.Success -> handleSuccess()
                is TaskState.Alert -> AlertInfo(it.m)
            }
        }
    }

    private fun handleLoading(state: Boolean) {
        if (state) loading.visible() else loading.gone()
    }

    private fun handleSuccess() {
        AlertDialog.Builder(this).apply {
            setMessage("terimakasih, anda sudah mengerjakan tugas")
            setPositiveButton("ya"){dialog, _ ->
                dialog.dismiss()
                startActivity(Intent(this@TaskActivity, MainActivity::class.java))
            }
        }.show()
    }

    override fun onResume() {
        super.onResume()
        getTasks()
    }
}