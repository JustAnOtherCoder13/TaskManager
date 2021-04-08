package com.picone.taskmanager.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.PopupMenu
import androidx.core.content.res.ResourcesCompat
import com.picone.core.domain.entity.CompleteTask
import com.picone.core.util.Constants
import com.picone.taskmanager.R
import java.util.*

object Constants {

    fun getImportanceToString(importance: Int, context: Context): String {
        return when (importance) {
            Constants.IMPORTANCE_IMPORTANT -> context.getString(R.string.important)
            Constants.IMPORTANCE_NORMAL -> context.getString(R.string.normal)
            else -> context.getString(R.string.unimportant)
        }
    }

     fun setProgressDrawable(start:Date?,close: Date?, context: Context): Drawable? {
        return when(close){
            null ->
                if (start!=null)ResourcesCompat.getDrawable(context.resources,R.drawable.custom_round_in_progress,null)
                else ResourcesCompat.getDrawable(context.resources,R.drawable.custom_round_todo,null)
            else -> ResourcesCompat.getDrawable(context.resources,R.drawable.custom_round_work_done,null)
        }
    }

    fun getDelayBetweenTwoDateInDaysToString(startDate:Date?,endDate:Date?):String{
        return if (startDate==null||endDate==null)"undifined"
        else ((endDate.time - startDate.time) /1000/60/60/24).toString()+" days"
    }

    fun showPopUp(view: View,menuId:Int,context: Context,itemClickListener: PopupMenu.OnMenuItemClickListener) {
        val popupMenu = PopupMenu(context, view)
        val inflater = popupMenu.menuInflater
        inflater.inflate(menuId, popupMenu.menu)
        popupMenu.show()
        popupMenu.setOnMenuItemClickListener (itemClickListener)
    }
}