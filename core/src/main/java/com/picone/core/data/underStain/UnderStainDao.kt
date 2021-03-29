package com.picone.core.data.underStain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.picone.core.domain.entity.UnderStain
import kotlinx.coroutines.flow.Flow

@Dao
interface UnderStainDao {

    @Query("SELECT*FROM under_stain_table WHERE under_stain_table.taskId= :taskId")
    fun getAllUnderStainForTaskId(taskId:Int): Flow<List<UnderStain>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewUnderStain(underStain: UnderStain)
}