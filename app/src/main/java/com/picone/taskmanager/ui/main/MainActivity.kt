package com.picone.taskmanager.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.picone.core.domain.entity.Category
import com.picone.taskmanager.R
import com.picone.taskmanager.databinding.ActivityMainBinding
import com.picone.taskmanager.ui.viewModels.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped

@ActivityScoped
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val fakeCategory = Category(3,"violet","test")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val addButton = binding.addButton
        addButton.setOnClickListener {
            categoryViewModel.addNewCategory(fakeCategory)
        }
        categoryViewModel.allCategories.observe(this, {
            Log.i("TAG", "onCreate: $it  ${binding.addButton}")
        })
    }

}