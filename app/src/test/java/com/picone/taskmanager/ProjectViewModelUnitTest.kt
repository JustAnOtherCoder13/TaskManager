package com.picone.taskmanager

import com.picone.core.data.Generator
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class ProjectViewModelUnitTest : BaseUnitTest() {

    @Test
    fun assertNotNull() {
        Assert.assertNotNull(projectViewModel)
        Assert.assertTrue(projectViewModel.mAllProjectsMutableLD.hasObservers())
        Assert.assertTrue(projectViewModel.mProjectForIdMutableLD.hasObservers())
        Assert.assertNotNull(projectViewModel.mAllProjectsMutableLD)
        Assert.assertNotNull(projectViewModel.mProjectForIdMutableLD)
    }

    @Test
    fun getAllProjectsShouldReturnListOfProjects() {
        runBlocking {
            Assert.assertEquals(
                Generator.generatedProjects().size,
                projectViewModel.mAllProjectsMutableLD.value?.size
            )
        }
    }

    @Test
    fun getProjectForIdShouldReturnTheGoodProject() {
        runBlocking {
            projectViewModel.getProjectForId(Generator.generatedProjects()[0].id)
            Assert.assertEquals(
                projectViewModel.mProjectForIdMutableLD.value?.name,
                Generator.generatedProjects()[0].name
            )
        }
    }

    @Test
    fun addCategoryShouldUpdateListOfCategories() {
        runBlocking {
            projectViewModel.addNewProject(projectToAdd)
            Assert.assertEquals(
                4,
                projectViewModel.mAllProjectsMutableLD.value?.size
            )
            Assert.assertTrue(
                projectViewModel.mAllProjectsMutableLD.value?.lastOrNull()?.name.equals(
                    TEST_PROJECT_NAME
                )
            )
        }
    }
}