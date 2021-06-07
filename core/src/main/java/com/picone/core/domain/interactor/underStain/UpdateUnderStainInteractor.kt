package com.picone.core.domain.interactor.underStain

import com.picone.core.data.underStain.UnderStainRepository
import com.picone.core.domain.entity.UnderStain
import javax.inject.Inject

class UpdateUnderStainInteractor @Inject constructor(private val underStainRepository: UnderStainRepository) {

    suspend fun updateUnderStain(underStain : UnderStain){
        underStainRepository.updateUnderStain(underStain)
    }
}