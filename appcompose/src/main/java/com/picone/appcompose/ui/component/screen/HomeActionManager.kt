package com.picone.appcompose.ui.component.screen

import androidx.compose.runtime.Composable
import com.picone.appcompose.ui.component.manager.action.navAction.NavActionManager
import com.picone.appcompose.ui.component.manager.action.navAction.NavActions
import com.picone.appcompose.ui.component.manager.action.navAction.NavigationDirections
import com.picone.appcompose.ui.component.screen.home.HomeActions
import com.picone.core.domain.entity.Task
import com.picone.newArchitectureViewModels.HomeScreenViewModel

class HomeActionManager(
    private val homeScreenViewModel: HomeScreenViewModel,
    private val navActionManager: NavActionManager
) {
    //val homeEventManager : HomeEventManager = HomeEventManager()


    /*@Composable
    fun onCreateScreenState(){
        HomeActions.updateScreenState.DoAction {
            homeEventManager.triggerEvent(HomeActions.updateScreenState)
        }
    }*/

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
            navActionManager.navigate(NavActions.NAV_TO_ADD)
        }
    }

    fun navigateToDetailOnTaskClicked(selectedTask:Task){
        HomeActions.navigateToDetailOnTaskClicked.doAction {
            navActionManager.navigate(NavActions.NAV_TO_DETAIL,selectedTask)
        }
    }

    fun onBottomNavItemSelected(selectedItem : String){
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
            navActionManager.navigate(NavActions.NAV_TO_HOME)
        }
    }

    private fun navigateToProjectOnNavItemClicked(){
        HomeActions.navigateToProjectOnNavItemClicked.doAction {
            navActionManager.navigate(NavActions.NAV_TO_PROJECT)
        }
    }
}