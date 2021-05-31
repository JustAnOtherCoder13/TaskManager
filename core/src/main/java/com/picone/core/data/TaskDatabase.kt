package com.picone.core.data

import android.content.ContentValues
import android.content.Context
import androidx.room.*
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
import com.picone.core.util.Constants.PROJECT_TABLE_NAME
import com.picone.core.util.Constants.TASK_TABLE_NAME
import com.picone.core.util.Constants.UNDER_STAIN_TABLE_NAME
import com.picone.core.util.DateTypeConverter

@Database(
    entities = [Category::class, Project::class, Task::class, UnderStain::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateTypeConverter::class)
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
            Room.databaseBuilder(appContext, TaskDatabase::class.java, "task_database.db")
                .fallbackToDestructiveMigration()
                //.addCallback(CALLBACK)
                .build()

        private val CALLBACK = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                createCategories(db)
                createProjects(db)
                createTasks(db)
                createUnderStains(db)
            }
        }

        private fun createCategories(db: SupportSQLiteDatabase){
            val contentValues = ContentValues()

            for (category in Generator.generatedCategories()){
                contentValues.put("id", category.id)
                contentValues.put("color", category.color)
                contentValues.put("name", category.name)

                db.insert(CATEGORY_TABLE_NAME, OnConflictStrategy.IGNORE, contentValues)
            }
        }

        private fun createProjects(db: SupportSQLiteDatabase){
            val contentValues = ContentValues()

            for (project in Generator.generatedProjects()){
                contentValues.put("id", project.id)
                contentValues.put("categoryId", project.categoryId)
                contentValues.put("name", project.name)
                contentValues.put("description", project.description)

                db.insert(PROJECT_TABLE_NAME, OnConflictStrategy.IGNORE, contentValues)
            }
        }

        private fun createTasks(db: SupportSQLiteDatabase){
            val contentValues = ContentValues()

            for (task in Generator.generatedTasks()){
                contentValues.put("id", task.id)
                contentValues.put("categoryId", task.categoryId)
                contentValues.put("name", task.name)
                contentValues.put("importance", task.importance)
                contentValues.put("description", task.description)
                contentValues.put("creation", task.creation.time)
                contentValues.put("deadLine", task.deadLine?.time)
                contentValues.put("start", task.start?.time)
                contentValues.put("close", task.close?.time)

                db.insert(TASK_TABLE_NAME, OnConflictStrategy.IGNORE, contentValues)
            }
        }
        private fun createUnderStains(db: SupportSQLiteDatabase){
            val contentValues = ContentValues()

            for (underStain in Generator.generatedUnderStains()){
                contentValues.put("id", underStain.id)
                contentValues.put("taskId", underStain.taskId)
                contentValues.put("name", underStain.name)
                contentValues.put("description", underStain.description)
                contentValues.put("start", underStain.start?.time)
                contentValues.put("deadLine", underStain.deadLine?.time)
                contentValues.put("close", underStain.close?.time)

                db.insert(UNDER_STAIN_TABLE_NAME, OnConflictStrategy.IGNORE, contentValues)
            }
        }
    }
}