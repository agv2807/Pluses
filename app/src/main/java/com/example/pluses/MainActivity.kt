package com.example.pluses

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.example.pluses.calendar.CalendarFragment
import com.example.pluses.task_list.TaskListFragment
import com.example.pluses.task_list.add_item_menu.AddItemFragment

class MainActivity : AppCompatActivity() {

    private val tapBarFragment = TapBarFragment()
    val taskListFragment = TaskListFragment()
    private val calendarFragment = CalendarFragment()
    private var addItemFragment = AddItemFragment()

    private var container: FragmentContainerView? = null
    private var tapBarContainer: FragmentContainerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()

        addSupportFragmentManager(container = R.id.tap_bar_cont, fragment = tapBarFragment)
        addSupportFragmentManager(container = R.id.container, fragment = taskListFragment)
    }

    private fun setupViews() {
        container = findViewById(R.id.container)
        tapBarContainer = findViewById(R.id.tap_bar_cont)
    }

    private fun addSupportFragmentManager(container: Int, fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            add(container, fragment)
            commit()
        }
    }

    private fun replaceSupportFragmentManager(container: Int, fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(container, fragment)
            commit()
        }
    }

    private fun removeFragmentManager(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            remove(fragment)
            commit()
        }
    }

    fun routeToMain() {
        replaceSupportFragmentManager(container = R.id.container, fragment = taskListFragment)
    }

    fun routeToCalendar() {
        replaceSupportFragmentManager(container = R.id.container, fragment = calendarFragment)
    }

    fun openAddMenu() {
        removeFragmentManager(fragment = tapBarFragment)
        addSupportFragmentManager(container = R.id.add_menu_cont, fragment = addItemFragment)
    }

    fun closeAddMenu() {
        addSupportFragmentManager(container = R.id.tap_bar_cont, tapBarFragment)
        removeFragmentManager(fragment = addItemFragment)
    }
}