package com.picone.core.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.picone.core.data.category.CategoryDao
import com.picone.core.domain.entity.Category

@Database(entities = [Category::class], version = 1,exportSchema = false)
abstract class TaskDatabase: RoomDatabase() {
    abstract fun categoryDao():CategoryDao

    companion object {
        @Volatile private var instance: TaskDatabase? = null

        fun getDatabase(context: Context): TaskDatabase =
            instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it } }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, TaskDatabase::class.java, "characters")
                .fallbackToDestructiveMigration()
                .build()
    }
}