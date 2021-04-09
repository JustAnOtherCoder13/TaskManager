package com.picone.taskmanager.ui.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.picone.core.domain.entity.*
import com.picone.core.util.Constants
import com.picone.core.util.Constants.ADD_PROJECT
import com.picone.core.util.Constants.ADD_TASK
import com.picone.core.util.Constants.ADD_UNDER_STAIN
import com.picone.core.util.Constants.FIRST_ELEMENT
import com.picone.core.util.Constants.IMPORTANCE_IMPORTANT
import com.picone.core.util.Constants.IMPORTANCE_NORMAL
import com.picone.core.util.Constants.IMPORTANCE_UNIMPORTANT
import com.picone.core.util.Constants.MY_DAY
import com.picone.core.util.Constants.MY_MONTH
import com.picone.core.util.Constants.MY_YEAR
import com.picone.core.util.Constants.PROJECT_ID
import com.picone.core.util.Constants.TASK_ID
import com.picone.core.util.Constants.WHAT_IS_ADD
import com.picone.taskmanager.R
import com.picone.taskmanager.databinding.FragmentAddBinding
import com.picone.taskmanager.ui.viewModels.*
import com.picone.taskmanager.ui.viewModels.BaseViewModel.Companion.completionStateMutableLD
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AddFragment : DatePickerDialog.OnDateSetListener, Fragment() {
    private var _binding: FragmentAddBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var importanceList: List<String>
    private val categoryList: MutableList<String> = mutableListOf()
    private val categoryViewModel: CategoryViewModel by activityViewModels()
    private val taskViewModel: TaskViewModel by activityViewModels()
    private val projectViewModel: ProjectViewModel by activityViewModels()
    private val underStainViewModel: UnderStainViewModel by activityViewModels()
    private lateinit var datePickerDialog: DatePickerDialog
    private var allCategories: List<Category> = listOf()
    private var allProjects: List<Project> = listOf()
    private var allTasks: List<CompleteTask> = listOf()
    private var allUnderStains: List<UnderStain> = mutableListOf()
    private lateinit var mNavController: NavController


    //TODO hide unused information
    //TODO force setting obligate information
    //TODO delete project when transform in task

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        var monthToPass = month + 1
        if (monthToPass == 13) monthToPass = 0
        mBinding.addFragmentDeadLineDate.text =
            SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(
                try {
                    SimpleDateFormat(
                        "dd/MM/yyy",
                        Locale.FRANCE
                    ).parse("$dayOfMonth/$monthToPass/$year")
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        mNavController =
            activity?.let { Navigation.findNavController(it, R.id.nav_host_fragment) }!!
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        allCategories = categoryViewModel.mAllCategoriesMutableLD.value!!
        allProjects = projectViewModel.mAllProjectsMutableLD.value!!
        allTasks = taskViewModel.mAllTasksMutableLD.value!!
        completionStateMutableLD.value = BaseViewModel.Companion.CompletionState.START_STATE
        datePickerDialog = DatePickerDialog(
            view.context,
            this,
            MY_YEAR,
            MY_MONTH,
            MY_DAY
        )
        if (arguments?.get(PROJECT_ID)!=null) {
            val projectToTransform:Project = allProjects.filter {
                it.id==arguments?.getInt(PROJECT_ID)
            }[FIRST_ELEMENT]
            mBinding.categorySpinner.setText( allCategories.filter {
                it.id==projectToTransform.categoryId
            }[FIRST_ELEMENT].name)
            mBinding.addFragmentNameEditText.editText.setText(projectToTransform.name)
            mBinding.addFragmentDescriptionEditText.editText.setText(projectToTransform.description)
        }

        completionStateMutableLD.observe(viewLifecycleOwner, {
            when(it){
                BaseViewModel.Companion.CompletionState.UNDER_STAIN_ON_COMPLETE ->
                    mNavController.navigate(R.id.detailFragment,
                bundleOf(TASK_ID to arguments?.get(TASK_ID) as Int))

                BaseViewModel.Companion.CompletionState.TASK_ON_COMPLETE ->
                    mNavController.navigate(R.id.homeFragment)

                BaseViewModel.Companion.CompletionState.PROJECT_ON_COMPLETE ->
                    mNavController.navigate(R.id.projectFragment)
                else -> {}
            }

        })
        initImportanceDropDownMenu()
        initCategoryDropDownMenu()
        initClickListener()
    }

    private fun initClickListener() {
        mBinding.addFragmentDatePickerImageButton.setOnClickListener {
            datePickerDialog.show()
        }
        mBinding.addFragmentAddButton.setOnClickListener {
            when (arguments?.get(WHAT_IS_ADD)) {
                ADD_PROJECT -> addNewProject()
                ADD_TASK -> addNewTask()
                ADD_UNDER_STAIN -> addNewUnderStain()
            }
        }
    }

    private fun addNewUnderStain() {
        val selectedCompleteTask =
            allTasks.filter { it.task.id == arguments?.get(TASK_ID) as Int }[FIRST_ELEMENT]
        underStainViewModel.getAllUnderStainsForTask(selectedCompleteTask.task)
        allUnderStains = underStainViewModel.mAllUnderStainsForTaskMutableLD.value!!
        val underStain = UnderStain(
            selectedCompleteTask.underStainsForTask.size + 1,
            arguments?.get(TASK_ID) as Int,
            mBinding.addFragmentNameEditText.editText.text.toString(),
            mBinding.addFragmentDescriptionEditText.editText.text.toString(),
            null,
            if (mBinding.addFragmentDeadLineDate.text.trim().isNotEmpty()) SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.FRANCE
            ).parse(mBinding.addFragmentDeadLineDate.text.toString()) else null,
            null
        )
        underStainViewModel.addNewUnderStain(underStain)
        selectedCompleteTask.underStainsForTask.add(underStain)
    }

    private fun addNewTask() {
        taskViewModel.addNewTask(
            Task(
                allTasks.size + 1,
                allCategories.filter { it.name == mBinding.categorySpinner.text.toString() }[FIRST_ELEMENT].id,
                mBinding.addFragmentNameEditText.editText.text.toString(),
                mBinding.addFragmentDescriptionEditText.editText.text.toString(),
                getImportance(mBinding.importanceSpinner.text.toString()),
                Calendar.getInstance().time,
                null,
                if (mBinding.addFragmentDeadLineDate.text != null) SimpleDateFormat(
                    "dd/MM/yyyy",
                    Locale.FRANCE
                ).parse(mBinding.addFragmentDeadLineDate.text.toString()) else null,
                null
            )
        )
    }

    private fun addNewProject() {
        projectViewModel.addNewProject(
            Project(
                allProjects.size + 1,
                allCategories.filter { it.name == mBinding.categorySpinner.text.toString() }[FIRST_ELEMENT].id,
                mBinding.addFragmentNameEditText.editText.text.toString(),
                mBinding.addFragmentDescriptionEditText.editText.text.toString()
            )
        )
    }

    private fun getImportance(importance: String): Int {
        return when (importance) {
            resources.getString(R.string.important) -> IMPORTANCE_IMPORTANT
            resources.getString(R.string.normal) -> IMPORTANCE_NORMAL
            else -> IMPORTANCE_UNIMPORTANT
        }
    }

    private fun initImportanceDropDownMenu() {
        importanceList = listOf(
            resources.getString(R.string.important),
            resources.getString(R.string.normal),
            resources.getString(R.string.unimportant)
        )
        val importanceArrayAdapter = context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_dropdown_item,
                importanceList
            )
        }
        (mBinding.importanceSpinner as? AutoCompleteTextView)?.setAdapter(importanceArrayAdapter)
    }

    private fun initCategoryDropDownMenu() {
        for (category in categoryViewModel.mAllCategoriesMutableLD.value?.toList()!!) category.name?.let {
            categoryList.add(
                it
            )
        }
        val categoryArrayAdapter = context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_dropdown_item,
                categoryList
            )
        }
        (mBinding.categorySpinner as? AutoCompleteTextView)?.setAdapter(categoryArrayAdapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}