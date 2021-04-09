package com.picone.taskmanager.ui.fragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.picone.core.domain.entity.Project
import com.picone.taskmanager.databinding.ProjectTableRecyclerviewItemBinding

class ProjectTableAdapter(
    private var mAllProjects: List<Project>,
    val clickListener: (Project) -> Unit
) :
    RecyclerView.Adapter<ProjectTableAdapter.ViewHolder>() {
    private lateinit var binding: ProjectTableRecyclerviewItemBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProjectTableAdapter.ViewHolder {
        binding = ProjectTableRecyclerviewItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProjectTableAdapter.ViewHolder, position: Int) {
        val project: Project = mAllProjects[position]
        binding.projectItemName.text = project.name
        binding.projectItemDescription.text = project.description
        if (position == 0) {
            binding.separator.visibility = View.GONE
        }
        holder.itemView.setOnClickListener { clickListener(project) }
    }

    override fun getItemCount(): Int {
        return mAllProjects.size
    }

    inner class ViewHolder(binding: ProjectTableRecyclerviewItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun updateProjects(projects: List<Project>) {
        mAllProjects = projects
        notifyDataSetChanged()
    }
}