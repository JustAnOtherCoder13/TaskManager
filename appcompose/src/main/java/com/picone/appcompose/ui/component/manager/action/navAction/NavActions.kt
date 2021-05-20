package com.picone.appcompose.ui.component.manager.action.navAction

enum class NavActions {

    NAV_TO_HOME {
        override fun getNavAction(): NavAction {
            return object : NavAction { override val destination: String=NavigationDirections.Home.destination }
        }
    },
    NAV_TO_PROJECT{
        override fun getNavAction(): NavAction {
            return object : NavAction { override val destination: String=NavigationDirections.Project.destination }
        }
    },
    NAV_TO_DETAIL {
        override fun getNavAction(): NavAction {
            return object : NavAction { override val destination: String = NavigationDirections.Detail.destination }
        }
    },
    NAV_TO_ADD {
        override fun getNavAction(): NavAction {
           return object : NavAction { override val destination: String=NavigationDirections.Add.destination }
        }
    };

    abstract fun getNavAction() : NavAction
}