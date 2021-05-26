package com.picone.core.domain.interactor.underStain

import com.picone.core.data.underStain.UnderStainRepository
import com.picone.core.domain.entity.UnderStain
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllUnderStainsInteractor @Inject constructor(private val underStainRepository: UnderStainRepository) {

    fun getAllUnderStains() : Flow<List<UnderStain>> {
        return underStainRepository.getAllUnderStains()
    }
}