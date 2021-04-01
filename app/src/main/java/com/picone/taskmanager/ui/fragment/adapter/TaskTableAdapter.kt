package com.picone.taskmanager.ui.fragment.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.picone.core.domain.entity.CompleteTask
import com.picone.taskmanager.R
import com.picone.taskmanager.databinding.TaskTableRecyclerviewItemBinding

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
        binding.progressInformationImage.background = setProgressDrawable(task,holder.itemView)


        holder.itemView.setOnClickListener{clickListener(task)}
    }

    private fun setProgressDrawable(task: CompleteTask, view: View): Drawable? {
        return when(task.task.close){
            null ->
                if (task.task.start!=null)getDrawable(view.context,R.drawable.custom_round_in_progress)
                else getDrawable(view.context,R.drawable.custom_round_todo)
            else -> getDrawable(view.context,R.drawable.custom_round_work_done)
        }
    }
    fun getDrawable(context: Context, drawable: Int): Drawable? {
        return ResourcesCompat.getDrawable(context.getResources(), drawable, null)
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