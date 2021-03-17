package com.picone.core.data.underStain

import com.picone.core.data.TaskDatabase
import com.picone.core.domain.entity.UnderStain
import javax.inject.Inject

class UnderStainDaoImpl @Inject constructor(taskDatabase: TaskDatabase) {

    private val underStainDao = taskDatabase.underStainDao()

    suspend fun getAllUnderStainForTaskId(taskId:Int):List<UnderStain>{
        return underStainDao.getAllUnderStainForTaskId(taskId)
    }

    suspend fun addNewUnderStain(underStain: UnderStain){
        underStainDao.addNewUnderStain(underStain)
    }


}