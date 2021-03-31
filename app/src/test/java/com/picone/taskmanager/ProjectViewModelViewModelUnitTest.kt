package com.picone.taskmanager

import com.picone.core.data.Generator
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class ProjectViewModelViewModelUnitTest : BaseViewModelUnitTest() {

    @Test
    fun assertNotNull() {
        assertNotNull(projectViewModel)
        assertTrue(projectViewModel.mAllProjectsMutableLD.hasObservers())
        assertTrue(projectViewModel.mProjectForIdMutableLD.hasObservers())
        assertNotNull(projectViewModel.mAllProjectsMutableLD)
        assertNotNull(projectViewModel.mProjectForIdMutableLD)
    }

    @Test
    fun getAllProjectsShouldReturnListOfProjects() {
        runBlocking {
            assertEquals(
                Generator.generatedProjects().size,
                projectViewModel.mAllProjectsMutableLD.value?.size
            )
        }
    }

    @Test
    fun getProjectForIdShouldReturnTheGoodProject() {
        runBlocking {
            projectViewModel.getProjectForId(Generator.generatedProjects()[0].id)
            assertEquals(
                projectViewModel.mProjectForIdMutableLD.value?.name,
                Generator.generatedProjects()[0].name
            )
        }
    }

    @Test
    fun addCategoryShouldUpdateListOfCategories() {
        runBlocking {
            projectViewModel.addNewProject(projectToAdd)
            assertEquals(
                4,
                projectViewModel.mAllProjectsMutableLD.value?.size
            )
            assertTrue(
                projectViewModel.mAllProjectsMutableLD.value?.lastOrNull()?.name.equals(
                    TEST_PROJECT_NAME
                )
            )
        }
    }
}