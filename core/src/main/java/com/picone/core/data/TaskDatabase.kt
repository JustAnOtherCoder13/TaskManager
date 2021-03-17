package com.picone.core.data

import android.content.ContentValues
import android.content.Context
import androidx.room.Database
import androidx.room.OnConflictStrategy
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.picone.core.data.category.CategoryDao
import com.picone.core.data.project.ProjectDao
import com.picone.core.data.task.TaskDao
import com.picone.core.data.underStain.UnderStainDao
import com.picone.core.domain.entity.Category
import com.picone.core.domain.entity.Project
import com.picone.core.domain.entity.Task
import com.picone.core.domain.entity.UnderStain
import com.picone.core.util.Constants.CATEGORY_TABLE_NAME

@Database(
    entities = [Category::class, Project::class, Task::class, UnderStain::class],
    version = 1,
    exportSchema = false
)
abstract class TaskDatabase: RoomDatabase() {
    abstract fun categoryDao():CategoryDao
    abstract fun projectDao():ProjectDao
    abstract fun taskDao():TaskDao
    abstract fun underStainDao(): UnderStainDao

    companion object {
        @Volatile private var instance: TaskDatabase? = null

        fun getDatabase(context: Context): TaskDatabase =
            instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it } }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, TaskDatabase::class.java, "characters")
                .fallbackToDestructiveMigration()
                .addCallback(CALLBACK)
                .build()

        private val CALLBACK = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                createCategories(db)
            }
        }

        private fun createCategories(db: SupportSQLiteDatabase){
            val contentValues = ContentValues()

            for (category in Generator.generatedCategories()){
                contentValues.put("id", category.id)
                contentValues.put("color", category.color)
                contentValues.put("name", category.name)
            }
            db.insert(CATEGORY_TABLE_NAME, OnConflictStrategy.IGNORE, contentValues)
        }

    }
}