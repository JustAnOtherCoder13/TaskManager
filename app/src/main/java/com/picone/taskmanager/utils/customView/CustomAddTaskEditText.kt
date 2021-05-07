package com.picone.taskmanager.utils.customView

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.picone.taskmanager.R

class CustomAddTaskEditText@JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attributeSet, defStyle, defStyleRes) {
    var editText: EditText
    init {
        LayoutInflater.from(context)
            .inflate(R.layout.custom_add_task_edit_text, this, true)
        val title: TextView = findViewById(R.id.add_fragment_information_type)
        editText = findViewById(R.id.add_fragment_information_edit_text)
        val attributes: TypedArray = context.obtainStyledAttributes(attributeSet,R.styleable.CustomAddTaskEditText)

        title.text = attributes.getText(R.styleable.CustomAddTaskEditText_title)

        attributes.recycle()
    }
}