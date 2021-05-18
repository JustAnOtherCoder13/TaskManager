package com.picone.appcompose.ui.di

import com.picone.appcompose.ui.component.manager.action.nav.NavActionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class AppComposeModule {


  /*  @Provides
    fun provideNavManager(): NavActionManager{
        return NavActionManager()
    }*/

}