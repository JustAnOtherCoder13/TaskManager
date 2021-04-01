package com.picone.taskmanager.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.picone.core.util.Constants.IMPORTANCE_IMPORTANT
import com.picone.core.util.Constants.IMPORTANCE_NORMAL
import com.picone.core.util.Constants.IMPORTANCE_UNIMPORTANT
import com.picone.taskmanager.R
import com.picone.taskmanager.databinding.FragmentHomeBinding
import com.picone.taskmanager.ui.fragment.adapter.TaskTableAdapter
import com.picone.taskmanager.ui.viewModels.TaskViewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var mImportantTaskAdapter: TaskTableAdapter
    private lateinit var mNormalTaskAdapter: TaskTableAdapter
    private lateinit var mUnimportantTaskAdapter: TaskTableAdapter
    private lateinit var mNavController: NavController

    private val mTaskViewModel: TaskViewModel by activityViewModels()

    private lateinit var importanceList: List<String>

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
        mTaskViewModel.getAllTasks()
        mNavController = this.findNavController()
        initRecyclerViewAdapter()
        initRecyclerView()
        observeRecyclerViewValues()
        initFilterByImportanceDropDownMenu()
        setFilterByImportanceItemClickListener()
    }

    override fun onResume() {
        super.onResume()
        initFilterByImportanceDropDownMenu()
    }

    private fun setFilterByImportanceItemClickListener() {
        mBinding.filterSpinner.setOnItemClickListener { _, _, position, _ ->
            mBinding.homeFragmentTasksTableLayout.importantRecyclerView.visibility =
                if (importanceList[position] == resources.getString(R.string.important)
                    || importanceList[position] == resources.getString(R.string.all)
                ) VISIBLE
                else GONE

            mBinding.homeFragmentTasksTableLayout.normalRecyclerView.visibility =
                if (importanceList[position] == resources.getString(R.string.normal)
                    || importanceList[position] == resources.getString(R.string.all)
                ) VISIBLE
                else GONE

            mBinding.homeFragmentTasksTableLayout.unimportantRecyclerView.visibility =
                if (importanceList[position] == resources.getString(R.string.unimportant)
                    || importanceList[position] == resources.getString(R.string.all)
                ) VISIBLE
                else GONE
        }
    }

    private fun initFilterByImportanceDropDownMenu() {
        importanceList = listOf(
            resources.getString(R.string.important),
            resources.getString(R.string.normal),
            resources.getString(R.string.unimportant),
            resources.getString(R.string.all)
        )
        val importanceArrayAdapter = context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_dropdown_item,
                importanceList
            )
        }
        (mBinding.filterSpinner as? AutoCompleteTextView)?.setAdapter(importanceArrayAdapter)
    }

    private fun observeRecyclerViewValues() {
        mTaskViewModel.mAllTasksMutableLD.observe(viewLifecycleOwner, { it ->
            mImportantTaskAdapter.updateTasks(it.filter { it.task.importance == IMPORTANCE_IMPORTANT }
                .sortedBy { it.task.creation })
            mNormalTaskAdapter.updateTasks(it.filter { it.task.importance == IMPORTANCE_NORMAL }
                .sortedBy { it.task.creation })
            mUnimportantTaskAdapter.updateTasks(it.filter { it.task.importance == IMPORTANCE_UNIMPORTANT }
                .sortedBy { it.task.creation })
        })
    }

    private fun initRecyclerViewAdapter() {
        mImportantTaskAdapter =
            TaskTableAdapter(emptyList()) { mNavController.navigate(R.id.detailFragment) }
        mNormalTaskAdapter =
            TaskTableAdapter(emptyList()) { mNavController.navigate(R.id.detailFragment) }
        mUnimportantTaskAdapter =
            TaskTableAdapter(emptyList()) { mNavController.navigate(R.id.detailFragment) }
    }

    private fun initRecyclerView() {
        val recyclerViews = listOf(
            mBinding.homeFragmentTasksTableLayout.importantRecyclerView,
            mBinding.homeFragmentTasksTableLayout.normalRecyclerView,
            mBinding.homeFragmentTasksTableLayout.unimportantRecyclerView
        )
        for (recyclerView in recyclerViews) {
            recyclerView.tableRecyclerView.layoutManager =
                LinearLayoutManager(context)
            when (recyclerView.id) {
                mBinding.homeFragmentTasksTableLayout.importantRecyclerView.id
                -> recyclerView.tableRecyclerView.adapter = mImportantTaskAdapter
                mBinding.homeFragmentTasksTableLayout.normalRecyclerView.id
                -> recyclerView.tableRecyclerView.adapter = mNormalTaskAdapter
                mBinding.homeFragmentTasksTableLayout.unimportantRecyclerView.id
                -> recyclerView.tableRecyclerView.adapter = mUnimportantTaskAdapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}