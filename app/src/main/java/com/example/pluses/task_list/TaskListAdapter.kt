package com.example.pluses.task_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pluses.R
import com.example.pluses.task_list.models.Task

class TaskListAdapter(private var parentFragmentInterface: TaskListFragmentInterface): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = arrayListOf<Task>()

    fun setData(taskArray: ArrayList<Task>) {
        items = taskArray
        notifyDataSetChanged()
    }

    fun addItem(task: Task) {
        items.add(task)
        notifyDataSetChanged()
    }

    fun reloadItem(item: Task, position: Int) {
        items[position] = item
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        items.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holderCustom = holder as ViewHolder
        holderCustom.bind(task = items.elementAt(position))

        holder.itemView.setOnLongClickListener {
            parentFragmentInterface.openDialog(task = items.elementAt(position), position = position)
            true
        }

        holder.itemView.setOnClickListener {
            if (!items.elementAt(position).isDone) {
                parentFragmentInterface.setCountPluses(task = items.elementAt(position))
                items.removeAt(position)
                notifyDataSetChanged()
            } else {
                parentFragmentInterface.backCountPluses(task = items.elementAt(position))
                items.removeAt(position)
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(task: Task) {
            val text = itemView.findViewById<TextView>(R.id.task_text)
            text.text = task.taskText
            if (task.isDone) {
                text.setBackgroundResource(R.drawable.choose_item_background)
            }
        }
    }
}