package com.picone.taskmanager.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.picone.core.domain.entity.CompleteTask
import com.picone.core.domain.entity.UnderStain
import com.picone.core.util.Constants.medium
import com.picone.taskmanager.R
import com.picone.taskmanager.databinding.FragmentDetailBinding
import com.picone.taskmanager.ui.viewModels.TaskViewModel
import com.picone.taskmanager.utils.Constants.getDelayBetweenTwoDateInDaysToString
import com.picone.taskmanager.utils.Constants.getImportanceToString
import com.picone.taskmanager.utils.Constants.setProgressDrawable
import com.picone.taskmanager.utils.customView.CustomTaskInformation

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var mSelectedTask: CompleteTask
    private val mTaskViewModel: TaskViewModel by activityViewModels()

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
            it.task.id == arguments?.getInt("taskId")
        }?.get(0)!!
        initTaskInformationView()
        initTaskDescriptionView()
        mBinding.addButton.setOnClickListener {  }
        for (underStain in mSelectedTask.underStainsForTask){
            inflateNewUnderStainView(underStain)
        }
    }

    @SuppressLint("InflateParams")
    private fun inflateNewUnderStainView(underStain: UnderStain) {
        val inflater =
            activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(
            R.layout.fragment_detail_under_stain_description_layout,
            null
        )
        val linearLayoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        linearLayoutParams.setMargins(
            resources.getDimension(R.dimen.basic_marge).toInt(),
            resources.getDimension(R.dimen.little_marge).toInt(), 0, 0
        )
        rowView.layoutParams = linearLayoutParams
        mBinding.detailLinearLayout.addView(rowView, mBinding.root.childCount - 1)
        val title: CustomTaskInformation = rowView.findViewById(R.id.task_detail_under_stain_title)
        val expectedTime: CustomTaskInformation = rowView.findViewById(R.id.task_detail_under_stain_expected_time)
        val description: CustomTaskInformation = rowView.findViewById(R.id.task_detail_under_stain_description)
        val progressIndicator : ImageButton = rowView.findViewById(R.id.progress_information_image_button)

        title.informationTextView.text = underStain.name
        expectedTime.informationTextView.text = getDelayBetweenTwoDateInDaysToString(underStain.start,underStain.deadLine)
        description.informationTextView.text = underStain.description
        progressIndicator.background =
            context?.let { setProgressDrawable(underStain.start,underStain.close, it) }
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