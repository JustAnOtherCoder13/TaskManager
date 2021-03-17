package com.picone.taskmanager.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.picone.taskmanager.R
import com.picone.taskmanager.ui.viewModels.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped

@ActivityScoped
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val categoryViewModel: CategoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        categoryViewModel.setAllCategories()
        categoryViewModel.getAllCategories.observe(this, {
            Log.i("TAG", "onCreate: $it")
        })
    }

}