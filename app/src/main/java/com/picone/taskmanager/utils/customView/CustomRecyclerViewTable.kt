package com.picone.taskmanager.utils.customView

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.picone.taskmanager.R

class CustomRecyclerViewTable@JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attributeSet, defStyle, defStyleRes) {


    init {
        LayoutInflater.from(context)
            .inflate(R.layout.custom_recyclerview_table, this, true)
        val title:TextView = findViewById(R.id.table_title)
        val attributes:TypedArray = context.obtainStyledAttributes(attributeSet,R.styleable.CustomRecyclerViewTable)

        title.text = attributes.getText(R.styleable.CustomRecyclerViewTable_title)

        attributes.recycle()
    }
}