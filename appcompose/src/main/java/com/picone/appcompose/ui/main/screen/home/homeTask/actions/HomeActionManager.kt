package com.picone.appcompose.ui.main.screen.home.homeTask.actions

import com.picone.appcompose.ui.component.navAction.NavActionManager
import com.picone.appcompose.ui.component.navAction.NavObjects
import com.picone.core.domain.entity.Task
import com.picone.newArchitectureViewModels.HomeScreenViewModel

class HomeActionManager(
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
            navActionManager.navigate(NavObjects.Add)
        }
    }

    fun navigateToDetailOnTaskClicked(selectedTask:Task){
        HomeActions.navigateToDetailOnTaskClicked.doAction {
            navActionManager.navigate(NavObjects.Detail,selectedTask)
        }
    }

    fun onBottomNavItemSelected(selectedItem : String,homeScreenViewModel: HomeScreenViewModel){
        homeScreenViewModel.updateBottomNavSelectedItem(selectedItem)
        if (selectedItem == NavObjects.Home.destination){
            navigateToHomeOnNavItemClicked()
        }else if (selectedItem == NavObjects.Project.destination){
            navigateToProjectOnNavItemClicked()
        }else run {
            throw Exception("not allowed destination")
        }
    }
    private fun navigateToHomeOnNavItemClicked(){
        HomeActions.navigateToHomeTaskOnNavItemClicked.doAction {
            navActionManager.navigate(NavObjects.Home)
        }
    }

    private fun navigateToProjectOnNavItemClicked(){
        HomeActions.navigateToProjectOnNavItemClicked.doAction {
            navActionManager.navigate(NavObjects.Project)
        }
    }
}