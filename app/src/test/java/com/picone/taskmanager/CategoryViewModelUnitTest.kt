package com.picone.taskmanager


import com.picone.core.data.Generator
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class CategoryViewModelUnitTest : BaseUnitTest() {

    @Test
    fun assertNotNull() {
        assertNotNull(categoryViewModel)
        assertTrue(categoryViewModel.mAllCategoriesMutableLD.hasObservers())
        assertNotNull(categoryViewModel.mAllCategoriesMutableLD)
    }

    @Test
    fun getAllCategoriesShouldReturnListOfCategories() {
        runBlocking {
            assertEquals(
                Generator.generatedCategories().size,
                categoryViewModel.mAllCategoriesMutableLD.value?.size
            )
        }
    }

    @Test
    fun addCategoryShouldUpdateListOfCategories() {
        runBlocking {
            categoryViewModel.addNewCategory(categoryToAdd)
            assertEquals(
                3,
                categoryViewModel.mAllCategoriesMutableLD.value?.size
            )
            assertTrue(
                categoryViewModel.mAllCategoriesMutableLD.value?.lastOrNull()?.name.equals(
                    TEST_CATEGORY_NAME
                )
            )
        }
    }
}