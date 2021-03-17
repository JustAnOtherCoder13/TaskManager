package com.picone.core.data.underStain

import com.picone.core.data.TaskDatabase
import com.picone.core.domain.entity.UnderStain
import javax.inject.Inject

class UnderStainDaoImpl @Inject constructor(private val taskDatabase: TaskDatabase) {

    private val underStainDao = taskDatabase.underStainDao()

    fun getAllUnderStainForTaskId(taskId:Int):List<UnderStain>{
        return underStainDao.getAllUnderStainForTaskId(taskId)
    }

    suspend fun addNewUnderStain(underStain: UnderStain){
        underStainDao.addNewUnderStain(underStain)
    }


}