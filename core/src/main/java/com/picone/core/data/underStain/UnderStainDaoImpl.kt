package com.picone.core.data.underStain

import com.picone.core.data.TaskDatabase
import com.picone.core.domain.entity.UnderStain
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UnderStainDaoImpl @Inject constructor(taskDatabase: TaskDatabase) {

    private val underStainDao = taskDatabase.underStainDao()

    fun getAllUnderStainForTaskId(taskId: Int): Flow<List<UnderStain>> {
        return underStainDao.getAllUnderStainForTaskId(taskId)
    }
    fun getAllUnderStains() : Flow<List<UnderStain>>{
        return underStainDao.getAllUnderStains()
    }

    suspend fun addNewUnderStain(underStain: UnderStain) {
        underStainDao.addNewUnderStain(underStain)
    }

    suspend fun deleteUnderStain(underStain: UnderStain){
        underStainDao.deleteUnderStain(underStain)
    }

    suspend fun updateUnderStain(underStain: UnderStain){
        underStainDao.updateUnderStain(underStain)
    }
}