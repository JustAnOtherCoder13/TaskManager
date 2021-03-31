package com.picone.taskmanager.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.picone.core.util.Constants.IMPORTANCE_IMPORTANT
import com.picone.core.util.Constants.IMPORTANCE_NORMAL
import com.picone.core.util.Constants.IMPORTANCE_UNIMPORTANT
import com.picone.taskmanager.databinding.FragmentHomeBinding
import com.picone.taskmanager.ui.fragment.adapter.TaskTableAdapter
import com.picone.taskmanager.ui.viewModels.TaskViewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var mImportantTaskAdapter: TaskTableAdapter
    private lateinit var mNormalTaskAdapter: TaskTableAdapter
    private lateinit var mUnimportantTaskAdapter: TaskTableAdapter

    private val taskViewModel: TaskViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskViewModel.getAllTasks()
        mImportantTaskAdapter = TaskTableAdapter(emptyList())
        mNormalTaskAdapter = TaskTableAdapter(emptyList())
        mUnimportantTaskAdapter = TaskTableAdapter(emptyList())
        initRecyclerView()
        taskViewModel.mAllTasksMutableLD.observe(viewLifecycleOwner, { it ->
            mImportantTaskAdapter.updateTasks(it.filter { it.task.importance== IMPORTANCE_IMPORTANT }.sortedBy { it.task.creation })
            mNormalTaskAdapter.updateTasks(it.filter { it.task.importance== IMPORTANCE_NORMAL }.sortedBy { it.task.creation })
            mUnimportantTaskAdapter.updateTasks(it.filter { it.task.importance== IMPORTANCE_UNIMPORTANT }.sortedBy { it.task.creation })
        })
    }

    private fun initRecyclerView() {
        mBinding.homeFragmentTasksTableLayout.importantRecyclerView.tableRecyclerView.layoutManager =
            LinearLayoutManager(context)
        mBinding.homeFragmentTasksTableLayout.normalRecyclerView.tableRecyclerView.layoutManager =
            LinearLayoutManager(context)
        mBinding.homeFragmentTasksTableLayout.unimportantRecyclerView.tableRecyclerView.layoutManager =
            LinearLayoutManager(context)

        mBinding.homeFragmentTasksTableLayout.importantRecyclerView.tableRecyclerView.adapter =
            mImportantTaskAdapter
        mBinding.homeFragmentTasksTableLayout.normalRecyclerView.tableRecyclerView.adapter =
            mNormalTaskAdapter
        mBinding.homeFragmentTasksTableLayout.unimportantRecyclerView.tableRecyclerView.adapter =
            mUnimportantTaskAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}