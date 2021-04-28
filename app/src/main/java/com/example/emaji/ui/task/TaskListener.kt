package com.example.emaji.ui.task

import com.example.emaji.models.Task

interface TaskListener {
    fun click(task: Task)
}