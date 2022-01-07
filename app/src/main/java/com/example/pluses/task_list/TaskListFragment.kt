package com.example.pluses.task_list

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pluses.MainActivity
import com.example.pluses.R
import com.example.pluses.task_list.edit_item_menu.EditItemFragment
import com.example.pluses.task_list.models.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

interface TaskListFragmentInterface {
    var localContext: Context?
    fun openDialog(task: Task, position: Int)
    fun setCountPluses(task: Task)
    fun backCountPluses(task: Task)
}

class TaskListFragment : Fragment(), TaskListFragmentInterface {

    private val mainActivity: MainActivity
        get() = activity as MainActivity

    override var localContext: Context?
        get() {
            return context
        }
        set(value) {}

    private var taskListRv: RecyclerView? = null
    private var plusesListRv: RecyclerView? = null
    private var editButton: FloatingActionButton? = null
    private var countTasksTextView: TextView? = null
    private var completeDayButton: FloatingActionButton? = null

    val taskListAdapter = TaskListAdapter(this)
    val plusesListAdapter = TaskListAdapter(this)

    private val dataModel = TaskListViewModel()

    private lateinit var editItemFragment: EditItemFragment

    private var countPluses = 0
    private var plusesVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task_list, container, false)

        loadData()

        setupViews(view = view)

        dataModel.companion.items.observe(requireActivity(), {
            taskListAdapter.setData(taskArray = it)
        })

        taskListRv?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        taskListRv?.adapter = taskListAdapter

        plusesListRv?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        plusesListRv?.adapter = plusesListAdapter

        return view
    }

    override fun onPause() {
        super.onPause()
        saveData(values = dataModel.companion.items.value, count = countPluses)
    }

    private fun setupViews(view: View) {
        taskListRv = view.findViewById(R.id.task_list_rv)
        editButton = view.findViewById(R.id.new_task_button)
        countTasksTextView = view.findViewById(R.id.task_count_text)
        plusesListRv = view.findViewById(R.id.pluses_list_rv)
        completeDayButton = view.findViewById(R.id.complete_day_button)

        countTasksTextView?.text = "Плюсики   ${countPluses}"

        countTasksTextView?.setOnClickListener {
            if (plusesVisible) {
                plusesListRv?.visibility = View.GONE
                plusesVisible = !plusesVisible
                countTasksTextView?.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_down_24, 0)
            }
            else {
                plusesListRv?.visibility = View.VISIBLE
                plusesVisible = !plusesVisible
                countTasksTextView?.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_up_24,0)
            }
        }

        editButton?.setOnClickListener {
            mainActivity.openAddMenu()
        }

        completeDayButton?.setOnClickListener {
            confirmComplete()
        }
    }

    private fun saveData(values: ArrayList<Task>?, count: Int) {
        val sharedPreferences = activity?.getSharedPreferences("shared preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        val gson = Gson()
        if (values.isNullOrEmpty()) {
            editor?.putString("task list", null)
            editor?.putInt("count pluses", count)
            editor?.apply()
        } else {
            val json = gson.toJson(values)
            editor?.putString("task list", json)
            editor?.putInt("count pluses", count)
            editor?.apply()
        }

    }

    private fun loadData() {
        val sharedPreferences = activity?.getSharedPreferences("shared preferences", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences?.getString("task list", "")
        countPluses = sharedPreferences?.getInt("count pluses", 0)!!
        val type = object: TypeToken<ArrayList<Task>>() {
        }.type

        if (!json.isNullOrEmpty())
            dataModel.companion.items.value = gson.fromJson(json, type)
        else
            dataModel.companion.items.value = arrayListOf()
    }

    private fun openEditMenu(text: String, position: Int) {
        editItemFragment = EditItemFragment(text = text, position = position)
        requireActivity().supportFragmentManager.beginTransaction().apply {
            add(R.id.edit_cont, editItemFragment)
            commit()
        }
    }

    fun closeEditMenu() {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            remove(editItemFragment)
            commit()
        }
    }

    private fun confirmComplete() {
        val dialogFragment = AlertDialog.Builder(activity)
        dialogFragment.setTitle("Завершение дня")
            .setMessage("Завершить день?")
            .setPositiveButton("Завершить") {dialog, id ->
                reloadDay()
            }
            .setNegativeButton("Отмета") {dialog, id ->

            }
            .create().show()
    }

    private fun reloadDay() {
        saveData(values = null, count = 0)
        loadData()
        plusesListAdapter.setData(taskArray = arrayListOf())
        countTasksTextView?.text = "Плюсики   ${countPluses}"
    }

    override fun openDialog(task: Task, position: Int) {
        val dialogFragment = AlertDialog.Builder(activity, R.style.MyDialogTheme)
        dialogFragment.setTitle(task.taskText)
            .setPositiveButton("Удалить") { dialog, id ->
                taskListAdapter.deleteItem(position = position)
            }
            .setNegativeButton("Изменить") { dialog, id ->
                openEditMenu(text = task.taskText, position = position)
            }
            .create().show()
    }

    override fun setCountPluses(task: Task) {
        countTasksTextView?.text = "Плюсики   ${++countPluses}"
        task.isDone = true
        plusesListAdapter.addItem(task = task)
    }

    override fun backCountPluses(task: Task) {
        countTasksTextView?.text = "Плюсики   ${--countPluses}"
        task.isDone = false
        taskListAdapter.addItem(task = task)
    }
}