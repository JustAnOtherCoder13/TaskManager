package com.picone.core.domain.interactor.underStain

import com.picone.core.data.underStain.UnderStainRepository
import com.picone.core.domain.entity.UnderStain
import javax.inject.Inject

class GetAllUnderStainForTaskIdInteractor @Inject constructor(private val underStainRepository: UnderStainRepository) {

    suspend fun getAllUnderStainForTaskId(taskId:Int):List<UnderStain>{
        return underStainRepository.getAllUnderStainForTaskId(taskId)
    }
}