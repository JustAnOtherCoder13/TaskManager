package com.picone.appcompose.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import com.picone.appcompose.R
import com.picone.appcompose.ui.component.screen.Home
import com.picone.appcompose.ui.values.TaskManagerTheme
import com.picone.core.domain.entity.CompleteTask
import com.picone.viewmodels.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped
import androidx.compose.runtime.livedata.observeAsState

@ActivityScoped
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskViewModel.getAllTasks()
        taskViewModel.mAllTasksMutableLD.observe(this) {
            Log.i("TAG", "onCreate: " + (it.size))
        }
        setContent{
            TaskManagerTheme {
                val allTasks : List<CompleteTask> by taskViewModel.mAllTasksMutableLD.observeAsState(listOf())
                Home(allTasks)
            }
        }
    }
}