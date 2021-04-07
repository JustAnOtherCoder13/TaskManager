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
import com.picone.taskmanager.R
import com.picone.taskmanager.databinding.FragmentAddBinding
import com.picone.taskmanager.ui.viewModels.CategoryViewModel
import java.util.*

class AddFragment: DatePickerDialog.OnDateSetListener, Fragment() {
    private var _binding: FragmentAddBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var importanceList: List<String>
    private val categoryList: MutableList<String> = mutableListOf()
    private val categoryViewModel: CategoryViewModel by activityViewModels()
    private val calendar: Calendar = Calendar.getInstance()
    private lateinit var datePickerDialog: DatePickerDialog

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        TODO("Not yet implemented")
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater,container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        datePickerDialog = DatePickerDialog(view.context,this,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(
            Calendar.DAY_OF_MONTH))
        initImportanceDropDownMenu()
        initCategoryDropDownMenu()
        mBinding.addFragmentDatePickerImageButton.setOnClickListener{
            datePickerDialog.show()
        }
    }

    private fun initImportanceDropDownMenu() {
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