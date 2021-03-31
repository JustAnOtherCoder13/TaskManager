package com.picone.taskmanager

import com.picone.core.data.Generator
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TaskViewModelViewModelUnitTest : BaseViewModelUnitTest() {

    @Test
    fun assertNotNull() {
        assertNotNull(taskViewModel)
        assertNotNull(taskViewModel.mAllTasksMutableLD)
        assertNotNull(taskViewModel.mTasksForCategoryMutableLD)
        assertNotNull(taskViewModel.mTaskForIdMutableLD)
        assertTrue(taskViewModel.mAllTasksMutableLD.hasObservers())
        assertTrue(taskViewModel.mTasksForCategoryMutableLD.hasObservers())
        assertTrue(taskViewModel.mTaskForIdMutableLD.hasObservers())
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
    fun addTaskShouldUpdateListOfTasks() {
        runBlocking {
            taskViewModel.addNewTask(taskToAdd)
            assertEquals(
                4,
                taskViewModel.mAllTasksMutableLD.value?.size
            )
            assertTrue(
                taskViewModel.mAllTasksMutableLD.value?.lastOrNull()?.name.equals(
                    TEST_TASK_NAME
                )
            )
        }
    }
}