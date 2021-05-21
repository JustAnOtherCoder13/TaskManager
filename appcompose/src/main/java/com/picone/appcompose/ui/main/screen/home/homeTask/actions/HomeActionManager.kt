package com.picone.appcompose.ui.main.screen.home.homeTask.actions

import com.picone.appcompose.ui.component.manager.navAction.NavActionManager
import com.picone.appcompose.ui.component.manager.navAction.NavActions
import com.picone.appcompose.ui.component.manager.navAction.NavigationDirections
import com.picone.core.domain.entity.Task
import com.picone.newArchitectureViewModels.HomeScreenViewModel

class HomeActionManager(
    //private val homeScreenViewModel: HomeScreenViewModel,
    private val navActionManager: NavActionManager
) {
    //val homeEventManager : HomeEventManager = HomeEventManager()


    /*@Composable
    fun onCreateScreenState(){
        HomeActions.updateScreenState.DoAction {
            homeEventManager.triggerEvent(HomeActions.updateScreenState)
        }
    }*/

    fun topAppBarOpenPopUp(homeScreenViewModel: HomeScreenViewModel) {
        HomeActions.topAppBarAddItemButtonOpenPoUp.doAction {
            homeScreenViewModel.topAppBarAddItemButtonOpenPopUp()
        }
    }

    fun topAppBarClosePopUp(homeScreenViewModel: HomeScreenViewModel) {
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

    fun onBottomNavItemSelected(selectedItem : String,homeScreenViewModel: HomeScreenViewModel){
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