package com.picone.taskmanager.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.picone.taskmanager.databinding.FragmentProjectBinding
import com.picone.taskmanager.ui.fragment.adapter.ProjectTableAdapter
import com.picone.taskmanager.ui.viewModels.ProjectViewModel

class ProjectFragment : Fragment() {

    private var _binding: FragmentProjectBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var recyclerViewAdapter: ProjectTableAdapter
    private val projectViewModel: ProjectViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjectBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewAdapter = ProjectTableAdapter(emptyList()) {}
        mBinding.projectRecyclerView.tableRecyclerView.adapter = recyclerViewAdapter
        mBinding.projectRecyclerView.tableRecyclerView.layoutManager =
            LinearLayoutManager(context)
        projectViewModel.mAllProjectsMutableLD.observe(viewLifecycleOwner, {
            Log.i("TAG", "onViewCreated: ${it.size}")
            recyclerViewAdapter.updateProjects(it)
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}