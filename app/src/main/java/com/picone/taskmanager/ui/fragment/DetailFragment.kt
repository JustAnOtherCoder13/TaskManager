package com.picone.taskmanager.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.picone.core.domain.entity.CompleteTask
import com.picone.core.domain.entity.UnderStain
import com.picone.core.util.Constants.ADD_UNDER_STAIN
import com.picone.core.util.Constants.FIRST_ELEMENT
import com.picone.core.util.Constants.TASK_ID
import com.picone.core.util.Constants.WHAT_IS_ADD
import com.picone.core.util.Constants.medium
import com.picone.taskmanager.R
import com.picone.taskmanager.databinding.FragmentDetailBinding
import com.picone.taskmanager.utils.Constants.getDelayBetweenTwoDateInDaysToString
import com.picone.taskmanager.utils.Constants.getImportanceToString
import com.picone.taskmanager.utils.Constants.setProgressDrawable
import com.picone.taskmanager.utils.Constants.showPopUp
import com.picone.taskmanager.utils.customView.CustomTaskInformation
import com.picone.viewmodels.TaskViewModel
import com.picone.viewmodels.UnderStainViewModel
import java.util.*

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var mSelectedTask: CompleteTask
    private val mTaskViewModel: TaskViewModel by activityViewModels()
    private val mUnderStainViewModel: UnderStainViewModel by activityViewModels()
    private lateinit var mNavController: NavController

    //TODO implement click on tas to start task

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSelectedTask = mTaskViewModel.mAllTasksMutableLD.value?.filter {
            it.task.id == arguments?.getInt(TASK_ID)
        }?.get(FIRST_ELEMENT)!!
        for (underStain in mSelectedTask.underStainsForTask)inflateNewUnderStainView(underStain)
        mNavController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        initTaskInformationView()
        initTaskDescriptionView()
        val bundle = bundleOf(WHAT_IS_ADD to ADD_UNDER_STAIN, TASK_ID to mSelectedTask.task.id)
        mBinding.addButton.setOnClickListener { mNavController.navigate(R.id.addFragment, bundle) }
    }

    @SuppressLint("InflateParams")
    private fun inflateNewUnderStainView(underStain: UnderStain) {
        val inflater =
            activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(
            R.layout.fragment_detail_under_stain_description_layout,
            null
        )
        val linearLayoutParams: LinearLayout.LayoutParams = setLinearLayoutParams()
        rowView.layoutParams = linearLayoutParams
        mBinding.detailLinearLayout.addView(rowView, mBinding.root.childCount - 1)
        val underStainProgressButton: ImageButton =
            rowView.findViewById(R.id.progress_information_image_button)
        initUnderStainView(rowView, underStain)
        initUnderStainProgressButtonClickListener(underStainProgressButton, underStain)
    }

    private fun initUnderStainProgressButtonClickListener(
        underStainButton: ImageButton,
        underStain: UnderStain
    ) {
        if (underStain.start != null && underStain.close != null) return
        underStainButton.setOnClickListener {
            showPopUp(underStainButton, R.menu.under_stain_menu, requireContext()) {
                var message = ""
                when (it.itemId) {
                    R.id.start -> message = "would you like to start this under stain now ?"
                    R.id.close -> message = "would you like to close this under stain ?"
                }
                initAlertDialog(message, it, underStain, underStainButton)
                true
            }
        }
    }

    private fun initAlertDialog(
        message: String,
        it: MenuItem,
        underStain: UnderStain,
        underStainButton: ImageButton
    ) {
        val builder: MaterialAlertDialogBuilder? = context?.let { MaterialAlertDialogBuilder(it) }
        builder?.setMessage(message)
        builder?.setPositiveButton("OK") { _, _ ->
            when (it.itemId) {
                R.id.start -> {
                    underStain.start = Calendar.getInstance().time
                    mUnderStainViewModel.addNewUnderStain(underStain)
                    setProgressIndicatorBackground(underStainButton, underStain)
                }

                R.id.close -> {
                    underStain.close = Calendar.getInstance().time
                    mUnderStainViewModel.addNewUnderStain(underStain)
                    setProgressIndicatorBackground(underStainButton, underStain)
                }
                else -> Log.i("TAG", "inflateNewUnderStainView: none")
            }
        }
        builder?.show()
    }

    private fun setLinearLayoutParams(): LinearLayout.LayoutParams {
        val linearLayoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        linearLayoutParams.setMargins(
            resources.getDimension(R.dimen.basic_marge).toInt(),
            setTopMarginForUnderStain(),
            resources.getDimension(R.dimen.basic_marge).toInt(), 0
        )
        return linearLayoutParams
    }

    private fun setTopMarginForUnderStain(): Int {
        //as last view is button, have to check if before last is task description view
        val taskView: View = mBinding.root.getChildAt(mBinding.root.childCount - 2)
        return if (taskView.id == R.id.fragment_detail_task_description) {
            resources.getDimension(R.dimen.basic_marge).toInt()
        } else {
            resources.getDimension(R.dimen.little_marge).toInt()
        }
    }

    private fun initUnderStainView(
        rowView: View,
        underStain: UnderStain
    ) {
        val title: CustomTaskInformation = rowView.findViewById(R.id.task_detail_under_stain_title)
        val expectedTime: CustomTaskInformation =
            rowView.findViewById(R.id.task_detail_under_stain_expected_time)
        val description: CustomTaskInformation =
            rowView.findViewById(R.id.task_detail_under_stain_description)
        val progressIndicator: ImageButton =
            rowView.findViewById(R.id.progress_information_image_button)

        title.informationTextView.text = underStain.name
        expectedTime.informationTextView.text =
            getDelayBetweenTwoDateInDaysToString(underStain.start, underStain.deadLine)
        description.informationTextView.text = underStain.description

        setProgressIndicatorBackground(progressIndicator, underStain)
    }

    private fun setProgressIndicatorBackground(
        progressIndicator: ImageButton,
        underStain: UnderStain
    ) {
        progressIndicator.background =
            context?.let { setProgressDrawable(underStain.start, underStain.close, it) }
    }

    private fun initTaskDescriptionView() {
        mBinding.fragmentDetailTaskDescription.taskDetailExpectedTime.informationTextView.text =
            getDelayBetweenTwoDateInDaysToString(mSelectedTask.task.start, mSelectedTask.task.close)
        mBinding.fragmentDetailTaskDescription.taskDetailDescription.informationTextView.text =
            mSelectedTask.task.description
    }

    private fun initTaskInformationView() {
        mBinding.fragmentDetailTaskInformation.taskInformationTitle.text =
            mSelectedTask.task.name
        mBinding.fragmentDetailTaskInformation.taskDetailInformationStartDate.informationTextView.text =
            mSelectedTask.task.start?.medium
        mBinding.fragmentDetailTaskInformation.taskDetailInformationDeadline.informationTextView.text =
            mSelectedTask.task.deadLine?.medium
        mBinding.fragmentDetailTaskInformation.taskDetailInformationImportance.informationTextView.text =
            context?.let { getImportanceToString(mSelectedTask.task.importance, it) }
        mBinding.fragmentDetailTaskInformation.taskDetailInformationCategory.informationTextView.text =
            mSelectedTask.categoryForTask.name
        mBinding.fragmentDetailTaskInformation.progressInformationImage.background =
            context?.let {
                setProgressDrawable(
                    mSelectedTask.task.start,
                    mSelectedTask.task.close,
                    it
                )
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}