package com.picone.core.data.underStain

import com.picone.core.domain.entity.UnderStain
import javax.inject.Inject

class UnderStainRepository @Inject constructor(private val underStainDaoImpl: UnderStainDaoImpl) {

    fun getAllUnderStainForTaskId(taskId:Int):List<UnderStain>{
        return underStainDaoImpl.getAllUnderStainForTaskId(taskId)
    }

    suspend fun addNewUnderStain(underStain: UnderStain){
        underStainDaoImpl.addNewUnderStain(underStain)
    }
}