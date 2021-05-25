package com.picone.core.data.underStain

import androidx.room.*
import com.picone.core.domain.entity.UnderStain
import kotlinx.coroutines.flow.Flow

@Dao
interface UnderStainDao {

    @Query("SELECT*FROM under_stain_table WHERE under_stain_table.taskId= :taskId")
    fun getAllUnderStainForTaskId(taskId:Int): Flow<List<UnderStain>>

    @Query("SELECT*FROM under_stain_table")
    fun getAllUnderStains() : Flow<List<UnderStain>>

    @Insert
    suspend fun addNewUnderStain(underStain: UnderStain)

    @Delete
    suspend fun deleteUnderStain(underStain: UnderStain)

    @Update
    suspend fun updateUnderStain(underStain: UnderStain)
}