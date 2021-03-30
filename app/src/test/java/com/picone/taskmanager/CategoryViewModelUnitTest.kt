package com.picone.taskmanager


import com.picone.core.data.Generator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Test

class CategoryViewModelUnitTest : BaseUnitTest() {

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun assertNotNull() {
        assertNotNull(categoryViewModel)
        assertTrue(categoryViewModel.allCategoriesMutableLD.hasObservers())
        assertNotNull(categoryViewModel.allCategoriesMutableLD)
    }

    @Test
    fun getAllCategoriesShouldReturnListOfCategories() {
        runBlocking {
            assertEquals(
                Generator.generatedCategories().size,
                categoryViewModel.allCategoriesMutableLD.value?.size
            )
        }
    }

    @Test
    fun addCategoryShouldUpdateListOfCategories() {
        runBlocking {
            categoryViewModel.addNewCategory(categoryToAdd)
            assertEquals(
                3,
                categoryViewModel.allCategoriesMutableLD.value?.size
            )
            assertTrue(categoryViewModel.allCategoriesMutableLD.value?.lastOrNull()?.name.equals(TEST_NAME))
        }
    }
}