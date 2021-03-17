package com.picone.core.di

import android.content.Context
import androidx.room.Room
import com.picone.core.data.TaskDatabase
import com.picone.core.data.category.CategoryDao
import com.picone.core.data.category.CategoryDaoImpl
import com.picone.core.data.category.CategoryRepository
import com.picone.core.data.project.ProjectDaoImpl
import com.picone.core.data.project.ProjectRepository
import com.picone.core.data.task.TaskDaoImpl
import com.picone.core.data.task.TaskRepository
import com.picone.core.data.underStain.UnderStainDaoImpl
import com.picone.core.data.underStain.UnderStainRepository
import com.picone.core.domain.interactor.GetAllCategoriesInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class CoreModule {
    @Singleton
    @Provides
    fun provideTaskDatabase(@ApplicationContext context: Context):TaskDatabase {
        return TaskDatabase.getDatabase(context)
    }

    //---------------------------DAO--------------------------------------

    @Singleton
    @Provides
    fun provideCategoryDao(taskDatabase: TaskDatabase): CategoryDaoImpl {
        return CategoryDaoImpl(taskDatabase)
    }

    @Singleton
    @Provides
    fun provideProjectDao(taskDatabase: TaskDatabase): ProjectDaoImpl {
        return ProjectDaoImpl(taskDatabase)
    }

    @Singleton
    @Provides
    fun provideTaskDao(taskDatabase: TaskDatabase): TaskDaoImpl {
        return TaskDaoImpl(taskDatabase)
    }

    @Singleton
    @Provides
    fun provideUnderStainDao(taskDatabase: TaskDatabase): UnderStainDaoImpl {
        return UnderStainDaoImpl(taskDatabase)
    }


    //-----------------------------REPOSITORY--------------------------------
    @Provides
    fun provideCategoryRepository(categoryDaoImpl: CategoryDaoImpl): CategoryRepository {
        return CategoryRepository(categoryDaoImpl)
    }

    @Provides
    fun provideProjectRepository(projectDaoImpl: ProjectDaoImpl): ProjectRepository {
        return ProjectRepository(projectDaoImpl)
    }

    @Provides
    fun provideTaskRepository(taskDaoImpl: TaskDaoImpl): TaskRepository {
        return TaskRepository(taskDaoImpl)
    }

    @Provides
    fun provideUnderStainRepository(underStainDaoImpl: UnderStainDaoImpl): UnderStainRepository {
        return UnderStainRepository(underStainDaoImpl)
    }

    //------------------------------INTERACTORS----------------------------------

    @Provides
    fun provideGetAllCategories(categoryRepository: CategoryRepository): GetAllCategoriesInteractor {
        return GetAllCategoriesInteractor(categoryRepository)
    }
}