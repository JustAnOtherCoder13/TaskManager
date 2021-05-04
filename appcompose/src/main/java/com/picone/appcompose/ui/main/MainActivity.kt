package com.picone.appcompose.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.picone.appcompose.R
import com.picone.appcompose.ui.component.screen.Home
import com.picone.appcompose.ui.values.TaskManagerTheme
import com.picone.viewmodels.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped

@ActivityScoped
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            TaskManagerTheme {
                Home()
            }
        }
        taskViewModel.getAllTasks()

        taskViewModel.mAllTasksMutableLD.observe(this,){
            Log.i("TAG", "onCreate: "+ (it[0]))
        }
    }
}