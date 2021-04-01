package com.picone.taskmanager

import com.picone.core.data.Generator
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class UnderStainViewModelUnitTest : BaseViewModelUnitTest() {

    @Test
    fun assertNotNull() {
        assertNotNull(underStainViewModel)
        assertNotNull(underStainViewModel.mAllUnderStainsForTaskMutableLD)
        assertTrue(underStainViewModel.mAllUnderStainsForTaskMutableLD.hasObservers())
    }

    @Test
    fun getAllUnderStainsForTasksShouldReturnListOfUnderStains() {
        runBlocking {
            assertEquals(
                Generator.generatedUnderStains().filter {
                    it.taskId == Generator.generatedTasks()[0].id
                }.size,
                underStainViewModel.mAllUnderStainsForTaskMutableLD.value?.size
            )
        }
    }

    @Test
    fun addUnderStainShouldUpdateListOfUnderStains() {
        runBlocking {
            underStainViewModel.addNewUnderStain(underStainToAdd)
            assertEquals(
                3,
                underStainViewModel.mAllUnderStainsForTaskMutableLD.value?.size
            )
            assertTrue(
                underStainViewModel.mAllUnderStainsForTaskMutableLD.value?.lastOrNull()?.name.equals(
                    TEST_UNDER_STAIN_NAME
                )
            )
        }
    }

}