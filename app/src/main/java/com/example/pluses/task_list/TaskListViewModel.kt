package com.example.pluses.task_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pluses.task_list.models.Task


class TaskListViewModel: ViewModel() {

    companion object {

        val tasks = arrayListOf<Task>()

        val items: MutableLiveData<ArrayList<Task>> by lazy {
            MutableLiveData<ArrayList<Task>>()
        }

        init {
            items.value = tasks
        }
    }
    val companion = Companion

}

