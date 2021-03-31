package com.picone.taskmanager.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.picone.core.data.Generator
import com.picone.taskmanager.databinding.FragmentHomeBinding
import com.picone.taskmanager.ui.fragment.adapter.TaskTableAdapter
import com.picone.taskmanager.ui.viewModels.TaskViewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var tableAdapter: TaskTableAdapter
    private val taskViewModel: TaskViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tableAdapter = TaskTableAdapter(emptyList())
        val layoutManager = LinearLayoutManager(context)
        binding.homeFragmentTasksTableLayout.importantRecyclerView.tableRecyclerView.layoutManager =
            layoutManager
        binding.homeFragmentTasksTableLayout.importantRecyclerView.tableRecyclerView.adapter =
            tableAdapter

        taskViewModel.getAllTasks()
        taskViewModel.mAllTasksMutableLD.observe(viewLifecycleOwner, {
            tableAdapter.updateTasks(it)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}