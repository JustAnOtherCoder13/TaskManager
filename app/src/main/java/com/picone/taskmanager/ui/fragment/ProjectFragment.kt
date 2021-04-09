package com.picone.taskmanager.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.picone.core.util.Constants.ADD_TASK
import com.picone.core.util.Constants.PROJECT_ID
import com.picone.core.util.Constants.WHAT_IS_ADD
import com.picone.taskmanager.R
import com.picone.taskmanager.databinding.FragmentProjectBinding
import com.picone.taskmanager.ui.fragment.adapter.ProjectTableAdapter
import com.picone.taskmanager.ui.viewModels.ProjectViewModel

class ProjectFragment : Fragment() {

    private var _binding: FragmentProjectBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var recyclerViewAdapter: ProjectTableAdapter
    private val projectViewModel: ProjectViewModel by activityViewModels()
    private lateinit var mNavController: NavController

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
        mNavController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        recyclerViewAdapter = ProjectTableAdapter(emptyList()) {
            val builder: MaterialAlertDialogBuilder? = context?.let { MaterialAlertDialogBuilder(it) }
            builder?.setMessage("Would you like to transform \"${it.name}\" in task?")
            builder?.setPositiveButton("OK"){_,_ ->
                mNavController.navigate(R.id.addFragment, bundleOf(PROJECT_ID to it.id, WHAT_IS_ADD to ADD_TASK))
            }
            builder?.show()
        }
        mBinding.projectRecyclerView.tableRecyclerView.adapter = recyclerViewAdapter
        mBinding.projectRecyclerView.tableRecyclerView.layoutManager =
            LinearLayoutManager(context)
        projectViewModel.mAllProjectsMutableLD.observe(viewLifecycleOwner, {
            recyclerViewAdapter.updateProjects(it)
        })



    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}