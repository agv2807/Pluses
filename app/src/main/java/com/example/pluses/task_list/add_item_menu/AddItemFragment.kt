package com.example.pluses.task_list.add_item_menu

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pluses.MainActivity
import com.example.pluses.R
import android.app.Activity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.pluses.task_list.TaskListViewModel
import com.example.pluses.task_list.models.Task

class AddItemFragment() : Fragment() {

    private val mainActivity: MainActivity
        get() = activity as MainActivity

    private val viewModel = TaskListViewModel()

    private var saveButton: TextView? = null
    private var cancelButton: TextView? = null
    private var newTaskEditText: EditText? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_item, container, false)

        setupViews(view = view)

        return view
    }

    private fun setupViews(view: View) {
        saveButton = view.findViewById(R.id.save_button)
        cancelButton = view.findViewById(R.id.cancel_button)
        newTaskEditText = view.findViewById(R.id.new_task_edit_text)

        showSoftKeyboard(context = context, editText = newTaskEditText)

        cancelButton?.setOnClickListener {
            val inputManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            inputManager!!.hideSoftInputFromWindow(view.windowToken, 0)
            newTaskEditText?.text?.clear()

            mainActivity.closeAddMenu()
        }

        saveButton?.setOnClickListener {
            if (newTaskEditText?.text!!.isNotEmpty()) {
                viewModel.companion.items.value?.add(Task(newTaskEditText?.text.toString()))
                val inputManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                inputManager!!.hideSoftInputFromWindow(view.windowToken, 0)
                newTaskEditText?.text?.clear()

                mainActivity.closeAddMenu()
            }
        }
    }

    private fun showSoftKeyboard(context: Context?, editText: EditText?) {
        try {
            editText?.requestFocus()
            editText?.postDelayed({
                val keyboard = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                keyboard.showSoftInput(editText, 0)
            }, 200)
        } catch (npe: NullPointerException) {
        } catch (e: java.lang.Exception) {
        }
    }
}