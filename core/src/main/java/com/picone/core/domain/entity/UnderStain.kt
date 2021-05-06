package com.picone.core.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.picone.core.util.Constants
import com.picone.core.util.Constants.UNDER_STAIN_TABLE_NAME
import java.util.*

@Entity(tableName = UNDER_STAIN_TABLE_NAME,foreignKeys = [ForeignKey(
    entity = Task::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("taskId")
)])
data class UnderStain(
    @PrimaryKey(autoGenerate = true)val id:Int,

    @ColumnInfo(index = true)val taskId:Int,
    override val name: String = Constants.UNKNOWN,
    override val description: String = Constants.UNKNOWN,
    override var start:Date?,
    override var deadLine:Date?,
    override var close:Date?
    ):BaseTask(name, description, start, deadLine, close)