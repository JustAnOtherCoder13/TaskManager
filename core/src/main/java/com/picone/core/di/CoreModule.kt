package com.picone.core.di

import android.content.Context
import com.picone.core.data.TaskDatabase
import com.picone.core.data.category.CategoryDaoImpl
import com.picone.core.data.category.CategoryRepository
import com.picone.core.data.project.ProjectDaoImpl
import com.picone.core.data.project.ProjectRepository
import com.picone.core.data.task.TaskDaoImpl
import com.picone.core.data.task.TaskRepository
import com.picone.core.data.underStain.UnderStainDaoImpl
import com.picone.core.data.underStain.UnderStainRepository
import com.picone.core.domain.interactor.category.AddNewCategoryInteractor
import com.picone.core.domain.interactor.category.GetAllCategoriesInteractor
import com.picone.core.domain.interactor.project.AddNewProjectInteractor
import com.picone.core.domain.interactor.project.GetAllProjectInteractor
import com.picone.core.domain.interactor.project.GetProjectForIdInteractor
import com.picone.core.domain.interactor.task.AddNewTaskInteractor
import com.picone.core.domain.interactor.task.GetAllTasksInteractor
import com.picone.core.domain.interactor.task.GetAllTasksForCategoryIdInteractor
import com.picone.core.domain.interactor.task.GetTaskForIdInteractor
import com.picone.core.domain.interactor.underStain.AddNewUnderStainInteractor
import com.picone.core.domain.interactor.underStain.GetAllUnderStainForTaskIdInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class CoreModule {

    @Provides
    fun provideCoroutineScope() :CoroutineScope {return CoroutineScope(Dispatchers.IO)}

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

    //CATEGORY
    @Provides
    fun provideGetAllCategories(categoryRepository: CategoryRepository): GetAllCategoriesInteractor {
        return GetAllCategoriesInteractor(categoryRepository)
    }

    @Provides
    fun provideAddNewCategory(categoryRepository: CategoryRepository):AddNewCategoryInteractor{
        return AddNewCategoryInteractor(categoryRepository)
    }

    //PROJECT
    @Provides
    fun provideGetAllProject(projectRepository: ProjectRepository):GetAllProjectInteractor{
        return GetAllProjectInteractor(projectRepository)
    }

    @Provides
    fun provideGetProjectForId(projectRepository: ProjectRepository):GetProjectForIdInteractor{
        return GetProjectForIdInteractor(projectRepository)
    }

    @Provides
    fun provideAddNewProject(projectRepository: ProjectRepository):AddNewProjectInteractor{
        return AddNewProjectInteractor(projectRepository)
    }

    //TASK

    @Provides
    fun provideGetAllTasks(taskRepository: TaskRepository):GetAllTasksInteractor{
        return GetAllTasksInteractor(taskRepository)
    }

    @Provides
    fun provideGetTaskForId(taskRepository: TaskRepository):GetTaskForIdInteractor{
        return GetTaskForIdInteractor(taskRepository)
    }

    @Provides
    fun provideGetAllTasksForCategoryId(taskRepository: TaskRepository):GetAllTasksForCategoryIdInteractor{
        return GetAllTasksForCategoryIdInteractor(taskRepository)
    }

    @Provides
    fun provideAddNewTask(taskRepository: TaskRepository):AddNewTaskInteractor{
        return AddNewTaskInteractor(taskRepository)
    }

    //UNDER STAIN
    @Provides
    fun provideGetAllUnderStainsForTaskId(underStainRepository: UnderStainRepository):GetAllUnderStainForTaskIdInteractor{
        return GetAllUnderStainForTaskIdInteractor(underStainRepository)
    }

    @Provides
    fun provideAddNewUnderStain(underStainRepository: UnderStainRepository):AddNewUnderStainInteractor{
        return AddNewUnderStainInteractor(underStainRepository)
    }
}