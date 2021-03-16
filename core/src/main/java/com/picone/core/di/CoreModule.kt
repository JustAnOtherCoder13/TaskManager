package com.picone.core.di

import android.content.Context
import androidx.room.Room
import com.picone.core.data.TaskDatabase
import com.picone.core.data.category.CategoryDao
import com.picone.core.data.category.CategoryDaoImpl
import com.picone.core.data.category.CategoryRepository
import com.picone.core.domain.interactor.GetAllCategoriesInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Singleton


@InstallIn(ActivityComponent::class)
@Module
class CoreModule {
    @Singleton
    @Provides
    fun provideTaskDatabase(@ApplicationContext context: Context):TaskDatabase {
        return TaskDatabase.getDatabase(context)
    }

    @Singleton
    @Provides
    fun provideCategoryDao(taskDatabase: TaskDatabase): CategoryDaoImpl {
        return CategoryDaoImpl(taskDatabase)
    }

    @Provides
    fun provideCategoryRepository(categoryDaoImpl: CategoryDaoImpl): CategoryRepository {
        return CategoryRepository(categoryDaoImpl)
    }

    @Provides
    @ViewModelScoped
    fun provideGetAllCategories(categoryRepository: CategoryRepository): GetAllCategoriesInteractor {
        return GetAllCategoriesInteractor(categoryRepository)
    }
}