package com.picone.core.domain.interactor.underStain

import com.picone.core.data.underStain.UnderStainRepository
import com.picone.core.domain.entity.UnderStain
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllUnderStainForTaskIdInteractor @Inject constructor(private val underStainRepository: UnderStainRepository) {

     fun getAllUnderStainForTaskId(taskId:Int):Flow<List<UnderStain>>{
        return underStainRepository.getAllUnderStainForTaskId(taskId)
    }
}