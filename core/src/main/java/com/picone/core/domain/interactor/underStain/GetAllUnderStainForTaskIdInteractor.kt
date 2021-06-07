package com.picone.core.domain.interactor.underStain

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.picone.core.data.underStain.UnderStainRepository
import com.picone.core.domain.entity.UnderStain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllUnderStainForTaskIdInteractor @Inject constructor(private val underStainRepository: UnderStainRepository) {

     fun getAllUnderStainForTaskId(taskId:Int):Flow<List<State<UnderStain>>>{
        return underStainRepository.getAllUnderStainForTaskId(taskId)
            .map {allUnderStains ->
                val returnList : MutableList<State<UnderStain>> = mutableListOf()
                allUnderStains.forEach {underStain->
                    returnList.add(mutableStateOf(underStain))
                }
                returnList.toList()
            }
    }
}