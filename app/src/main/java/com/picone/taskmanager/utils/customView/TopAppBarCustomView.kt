package com.picone.taskmanager.utils.customView

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.picone.core.util.Constants.ADD_PROJECT
import com.picone.core.util.Constants.ADD_TASK
import com.picone.core.util.Constants.WHAT_IS_ADD
import com.picone.taskmanager.R
import com.picone.taskmanager.ui.main.MainActivity
import com.picone.taskmanager.utils.Constants.showPopUp

class TopAppBarCustomView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attributeSet, defStyle, defStyleRes) {


    private var addButton: ImageButton
    private lateinit var mNavController: NavController


    init {
        LayoutInflater.from(context)
            .inflate(R.layout.custom_top_app_bar, this,true)
        addButton = findViewById(R.id.top_app_bar_add_button)
    }

    fun initAddButton(context: MainActivity){
        mNavController = Navigation.findNavController(context,R.id.nav_host_fragment)
        addButton.setOnClickListener{
            showPopUp(addButton,R.menu.add_menu,context) {
                when (it.itemId) {
                    R.id.category -> Log.i("TAG", "showPopUp: category")
                    R.id.project -> safeNavigateToAdd(ADD_PROJECT)
                    R.id.task -> safeNavigateToAdd(ADD_TASK)
                }
                true
            }
        }
    }


    private fun safeNavigateToAdd(whatToAdd:Int){
        val bundle:Bundle= bundleOf(WHAT_IS_ADD to whatToAdd)
        if (mNavController.currentDestination?.id != R.id.addFragment)
            mNavController.navigate(R.id.addFragment,bundle)
    }
}