package com.picone.appcompose.ui.component.screen.home

import android.util.Log
import com.picone.appcompose.ui.component.manager.action.navAction.NavActionManager
import com.picone.appcompose.ui.component.manager.action.navAction.NavigationDirections
import com.picone.core.domain.entity.Task
import com.picone.newArchitectureViewModels.HomeScreenViewModel
import java.lang.Exception

class HomeActionManager(
    private val homeScreenViewModel: HomeScreenViewModel,
    private val navActionManager: NavActionManager
) {

    fun topAppBarOpenPopUp() {
        HomeActions.topAppBarAddItemButtonOpenPoUp.doAction {
            homeScreenViewModel.topAppBarAddItemButtonOpenPopUp()
        }
    }

    fun topAppBarClosePopUp() {
        HomeActions.topAppBarAddItemButtonClosePoUp.doAction {
            homeScreenViewModel.topAppBarAddItemButtonClosePopUp()
        }
    }

    fun navigateToAddOnPopUpItemSelected() {
        HomeActions.navigateToAddOnPopUpItemSelected.doAction {
            navActionManager.navigateToAdd()
        }
    }

    fun navigateToDetailOnTaskClicked(selectedTask:Task){
        HomeActions.navigateToDetailOnTaskClicked.doAction {
            navActionManager.navigateToDetail(selectedTask)
        }
    }

    fun onBottomNavItemSelected(selectedItem : String){
        Log.i("TAG", "onBottomNavItemSelected: $selectedItem")
        homeScreenViewModel.updateBottomNavSelectedItem(selectedItem)
        if (selectedItem == NavigationDirections.Home.destination){
            navigateToHomeOnNavItemClicked()
        }else if (selectedItem == NavigationDirections.Project.destination){
            navigateToProjectOnNavItemClicked()
        }else run {
            throw Exception("not allowed destination")
        }
    }
    private fun navigateToHomeOnNavItemClicked(){
        HomeActions.navigateToHomeTaskOnNavItemClicked.doAction {
            navActionManager.navigateToHome()
        }
    }

    private fun navigateToProjectOnNavItemClicked(){
        HomeActions.navigateToProjectOnNavItemClicked.doAction {
            navActionManager.navigateToProject()
        }
    }
}