package com.picone.taskmanager

import com.picone.core.data.Generator
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class TaskViewModelViewModelUnitTest : BaseViewModelUnitTest() {

    @Test
    fun assertNotNull() {
        Assert.assertNotNull(taskViewModel)
        Assert.assertNotNull(taskViewModel.mAllTasksMutableLD)
        Assert.assertNotNull(taskViewModel.mTasksForCategoryMutableLD)
        Assert.assertNotNull(taskViewModel.mTaskForIdMutableLD)
        Assert.assertTrue(taskViewModel.mAllTasksMutableLD.hasObservers())
        Assert.assertTrue(taskViewModel.mTasksForCategoryMutableLD.hasObservers())
        Assert.assertTrue(taskViewModel.mTaskForIdMutableLD.hasObservers())
    }

    @Test
    fun getAllTasksShouldReturnListOfTasks() {
        runBlocking {
            assertEquals(
                Generator.generatedTasks().size,
                taskViewModel.mAllTasksMutableLD.value?.size
            )
        }
    }

    @Test
    fun getTaskForIdShouldReturnTheGoodTask() {
        runBlocking {
            taskViewModel.getTaskForId(Generator.generatedTasks()[0].id)
            assertEquals(
                taskViewModel.mTaskForIdMutableLD.value?.name,
                Generator.generatedTasks()[0].name
            )
        }
    }

    @Test
    fun getTaskForCategoryShouldReturnTheGoodTasksList() {
        runBlocking {
            taskViewModel.getTasksForCategory(Generator.generatedCategories()[0])
            assertEquals(
                taskViewModel.mTasksForCategoryMutableLD.value?.size,
                Generator.generatedTasks().filter {
                    it.categoryId == Generator.generatedCategories()[0].id
                }.size
            )
            assertEquals(
                taskViewModel.mTasksForCategoryMutableLD.value?.get(0)?.categoryId,
                Generator.generatedTasks().filter {
                    it.categoryId == Generator.generatedCategories()[0].id
                }[0].categoryId
            )
        }
    }

    @Test
    fun addCategoryShouldUpdateListOfCategories() {
        runBlocking {
            taskViewModel.addNewTask(taskToAdd)
            Assert.assertEquals(
                4,
                taskViewModel.mAllTasksMutableLD.value?.size
            )
            Assert.assertTrue(
                taskViewModel.mAllTasksMutableLD.value?.lastOrNull()?.name.equals(
                    TEST_TASK_NAME
                )
            )
        }
    }

}