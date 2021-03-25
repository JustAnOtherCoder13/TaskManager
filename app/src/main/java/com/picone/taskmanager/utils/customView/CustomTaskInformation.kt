package com.picone.taskmanager.utils.customView

import android.content.Context
import android.content.res.TypedArray
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.picone.taskmanager.R

class CustomTaskInformation@JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attributeSet, defStyle, defStyleRes) {

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.custom_task_information, this, true)
        val informationType:TextView = findViewById(R.id.information_type)
        val attributes:TypedArray = context.obtainStyledAttributes(attributeSet,R.styleable.CustomTaskInformation)

        informationType.text = attributes.getText(R.styleable.CustomTaskInformation_title)

        attributes.recycle()
    }
}