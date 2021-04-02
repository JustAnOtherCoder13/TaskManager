package com.picone.taskmanager.utils.customView

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.picone.taskmanager.R
import com.picone.taskmanager.ui.main.MainActivity

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
            showPopUp(addButton)
        }
    }

    private fun showPopUp(view: View) {
        val popupMenu = PopupMenu(context, view)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.add_menu, popupMenu.menu)
        popupMenu.show()
        popupMenu.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.category -> Log.i("TAG", "showPopUp: category")
                R.id.project -> safeNavigateToAdd()
                R.id.task -> safeNavigateToAdd()
            }
            true
        }
    }
    private fun safeNavigateToAdd(){
        if (mNavController.currentDestination?.id != R.id.addFragment)
            mNavController.navigate(R.id.addFragment)
    }
}