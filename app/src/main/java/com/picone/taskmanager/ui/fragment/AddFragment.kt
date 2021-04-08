package com.picone.taskmanager.ui.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.picone.core.domain.entity.*
import com.picone.core.util.Constants.ADD_PROJECT
import com.picone.core.util.Constants.ADD_TASK
import com.picone.core.util.Constants.ADD_UNDER_STAIN
import com.picone.core.util.Constants.FIRST_ELEMENT
import com.picone.core.util.Constants.IMPORTANCE_IMPORTANT
import com.picone.core.util.Constants.IMPORTANCE_NORMAL
import com.picone.core.util.Constants.IMPORTANCE_UNIMPORTANT
import com.picone.core.util.Constants.WHAT_IS_ADD
import com.picone.taskmanager.R
import com.picone.taskmanager.databinding.FragmentAddBinding
import com.picone.taskmanager.ui.viewModels.CategoryViewModel
import com.picone.taskmanager.ui.viewModels.ProjectViewModel
import com.picone.taskmanager.ui.viewModels.TaskViewModel
import com.picone.taskmanager.ui.viewModels.UnderStainViewModel
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
    private val calendar: Calendar = Calendar.getInstance()
    private lateinit var datePickerDialog: DatePickerDialog
    private var allCategories: List<Category> = listOf()
    private var allProjects: List<Project> = listOf()
    private var allTasks: List<CompleteTask> = listOf()
    private var allUnderStains : List<UnderStain>? = mutableListOf()

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        var monthToPass = month+1
        if (monthToPass==13)monthToPass=0
        mBinding.addFragmentDeadLineDate.text =
            SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(try {
                SimpleDateFormat("dd/MM/yyy", Locale.FRANCE).parse("$dayOfMonth/$monthToPass/$year")
            }catch (e:ParseException){
                e.printStackTrace()
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        allCategories = categoryViewModel.mAllCategoriesMutableLD.value!!
        allProjects = projectViewModel.mAllProjectsMutableLD.value!!
        allTasks = taskViewModel.mAllTasksMutableLD.value!!
        datePickerDialog = DatePickerDialog(
            view.context,
            this,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        initImportanceDropDownMenu()
        initCategoryDropDownMenu()
        mBinding.addFragmentDatePickerImageButton.setOnClickListener {
            datePickerDialog.show()
        }
        mBinding.addFragmentAddButton.setOnClickListener {
            when (arguments?.get(WHAT_IS_ADD)) {
                ADD_PROJECT -> {
                    projectViewModel.addNewProject(
                        Project(
                            allProjects.size+1,
                            allCategories.filter { it.name == mBinding.categorySpinner.text.toString() }[FIRST_ELEMENT].id,
                            mBinding.addFragmentNameEditText.editText.toString(),
                            mBinding.addFragmentDescriptionEditText.editText.toString()
                        )
                    )
                }
                ADD_TASK -> {

                            taskViewModel.addNewTask(
                                Task(
                                    allTasks.size+1,
                                    allCategories.filter { it.name == mBinding.categorySpinner.text.toString() }[FIRST_ELEMENT].id,
                                    mBinding.addFragmentNameEditText.editText.text.toString(),
                                    mBinding.addFragmentDescriptionEditText.editText.text.toString(),
                                    getImportance(mBinding.importanceSpinner.text.toString()),
                                    Calendar.getInstance().time,
                                    null,
                                    SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).parse(mBinding.addFragmentDeadLineDate.text.toString()),
                                    null
                                )
                            )
                }
                ADD_UNDER_STAIN -> {
                    val selectedCompleteTask = allTasks.filter { it.task.id== arguments?.get("taskId") as Int}[FIRST_ELEMENT]
                    underStainViewModel.getAllUnderStainsForTask(selectedCompleteTask.task)
                    allUnderStains = underStainViewModel.mAllUnderStainsForTaskMutableLD.value
                    underStainViewModel.addNewUnderStain(
                        UnderStain(
                            allUnderStains?.size?.plus(1)?:1,
                            arguments?.get("taskId") as Int,
                            mBinding.addFragmentNameEditText.editText.text.toString(),
                            mBinding.addFragmentDescriptionEditText.editText.text.toString(),
                            null,
                            SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).parse(mBinding.addFragmentDeadLineDate.text.toString()),
                            null
                            )
                    )
                }
            }
        }
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