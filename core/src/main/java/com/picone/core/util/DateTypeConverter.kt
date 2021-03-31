package com.picone.core.util

import androidx.room.TypeConverter
import java.text.DateFormat
import java.util.*

class DateTypeConverter {

        @TypeConverter
        fun fromTimestamp(value: Long?): Date? {
            return value?.let { Date(it) }
        }

        @TypeConverter
        fun dateToTimestamp(date: Date?): Long? {
            return date?.time
        }
}