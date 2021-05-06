package com.picone.appcompose.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import com.picone.appcompose.ui.values.TaskManagerTheme
import com.picone.core.domain.entity.CompleteTask
import com.picone.viewmodels.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped
import androidx.compose.runtime.livedata.observeAsState
import com.picone.appcompose.ui.component.screen.Detail
import com.picone.core.domain.entity.Task
import java.util.*

@ActivityScoped
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val taskViewModel: TaskViewModel by viewModels()
    private val task : Task = Task(0,0,"task not found","",0,Calendar.getInstance().time,null,null,null)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskViewModel.getAllTasks()
        taskViewModel.mAllTasksMutableLD.observe(this) {
            Log.i("TAG", "onCreate: " + (it.size))
        }
        setContent{
            TaskManagerTheme {
                val allTasks : List<CompleteTask> by taskViewModel.mAllTasksMutableLD.observeAsState(listOf())
                if (allTasks.isEmpty()) Task(0,0,"task not found","",0,Calendar.getInstance().time,null,null,null)else allTasks[0]
                //Home(allTasks)
                Detail(if (allTasks.isEmpty()) CompleteTask(task) else allTasks[0])
            }
        }
    }
}