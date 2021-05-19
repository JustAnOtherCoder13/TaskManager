package com.picone.appcompose.ui.component.manager.action.navAction

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.picone.appcompose.ui.component.manager.action.Action
import java.lang.Exception

interface NavAction:Action {

    val destination: String
        get() = ""
    val argument: String
        get() = ""

    fun doNavAction(navController: NavController){
        doAction {
            try {
                if (argument.trim().isNotEmpty())
                    navController.navigate("$destination/${argument}"){
                        popUpTo(navController.graph.startDestination) {

                        }
                        launchSingleTop = true
                    }
                else
                    navController.navigate(destination){
                        popUpTo(navController.graph.startDestination) {
                            //todo change bottomNavItem here
                        }
                        launchSingleTop = true

                    }
            }catch (e:Exception){
                Log.e("TAG", "doNavAction: if KEY is define in NavigationDirection you have to pass argument in NavActionImpl ",e )
            }

        }
    }
}