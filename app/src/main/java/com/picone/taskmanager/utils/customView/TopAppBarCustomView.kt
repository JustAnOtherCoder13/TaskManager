package com.picone.taskmanager.utils.customView

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.picone.taskmanager.R

class TopAppBarCustomView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attributeSet, defStyle, defStyleRes) {

    private var addButton: ImageButton

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.custom_top_app_bar, this,true)

        addButton = findViewById(R.id.top_app_bar_add_button)
        addButton.setOnClickListener {
            Log.i("TAG", "init: button clicked")
        }
    }
}