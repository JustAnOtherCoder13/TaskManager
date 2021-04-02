package com.picone.taskmanager.ui.fragment.adapter

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.picone.core.domain.entity.CompleteTask
import com.picone.taskmanager.databinding.TaskTableRecyclerviewItemBinding
import com.picone.taskmanager.utils.Constants.setProgressDrawable

open class TaskTableAdapter(private var mAllTasks: List<CompleteTask>, val clickListener: (CompleteTask) -> Unit) :
    RecyclerView.Adapter<TaskTableAdapter.ViewHolder>() {

    private lateinit var binding: TaskTableRecyclerviewItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskTableAdapter.ViewHolder {
        binding = TaskTableRecyclerviewItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskTableAdapter.ViewHolder, position: Int) {
        val task : CompleteTask = mAllTasks[position]
        if (position==0){binding.separator.visibility=GONE}
        binding.taskItemTextView.text=task.task.name
        binding.progressInformationImage.background = setProgressDrawable(task.task.start,task.task.close,holder.itemView.context)
        holder.itemView.setOnClickListener{clickListener(task)}
    }

    override fun getItemCount(): Int {
        return mAllTasks.size
    }

    inner class ViewHolder(binding: TaskTableRecyclerviewItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun updateTasks(tasks:List<CompleteTask>){
        mAllTasks = tasks
        notifyDataSetChanged()
    }
}