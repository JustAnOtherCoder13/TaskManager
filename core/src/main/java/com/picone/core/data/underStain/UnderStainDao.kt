package com.picone.core.data.underStain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.picone.core.domain.entity.UnderStain

@Dao
interface UnderStainDao {

    @Query("SELECT*FROM under_stain_table WHERE under_stain_table.taskId= :taskId")
    suspend fun getAllUnderStainForTaskId(taskId:Int):List<UnderStain>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewUnderStain(underStain: UnderStain)
}