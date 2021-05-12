package com.picone.taskmanager.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.picone.taskmanager.R
import com.picone.taskmanager.databinding.ActivityMainBinding
import com.picone.viewmodels.CategoryViewModel
import com.picone.viewmodels.ProjectViewModel
import com.picone.viewmodels.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped

@ActivityScoped
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mNavController:NavController
    private val mTaskViewModel: TaskViewModel by viewModels()
    private val mCategoryViewModel: CategoryViewModel by viewModels()
    private val mProjectViewModel: ProjectViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mNavController  = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(mBinding.bottomNavBar,mNavController)
        mBinding.topAppBar.initAddButton(this)
        mTaskViewModel.getAllTasks()
        mCategoryViewModel.getAllCategories()
        mProjectViewModel.getAllProject()
    }


}