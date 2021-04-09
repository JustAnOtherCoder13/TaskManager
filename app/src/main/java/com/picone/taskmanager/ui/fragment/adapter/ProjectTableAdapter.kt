package com.picone.taskmanager.ui.fragment.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.picone.core.domain.entity.Project
import com.picone.taskmanager.databinding.TaskTableRecyclerviewItemBinding

class ProjectTableAdapter(
    private var mAllProjects: List<Project>,
    val clickListener: (Project) -> Unit
) :
    RecyclerView.Adapter<ProjectTableAdapter.ViewHolder>() {
    private lateinit var binding: TaskTableRecyclerviewItemBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProjectTableAdapter.ViewHolder {
        binding = TaskTableRecyclerviewItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProjectTableAdapter.ViewHolder, position: Int) {
        Log.i("TAG", "onBindViewHolder: ${mAllProjects.size}")
        val project: Project = mAllProjects[position]
        binding.taskItemTextView.text = project.name
        binding.progressInformationImage.visibility = View.GONE
        if (position == 0) {
            binding.separator.visibility = View.GONE
        }
        holder.itemView.setOnClickListener { clickListener(project) }
    }

    override fun getItemCount(): Int {
        return mAllProjects.size
    }

    inner class ViewHolder(binding: TaskTableRecyclerviewItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun updateProjects(projects: List<Project>) {
        mAllProjects = projects
        notifyDataSetChanged()
    }
}