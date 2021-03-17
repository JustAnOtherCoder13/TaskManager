package com.picone.core.data

import com.picone.core.domain.entity.Category

object Generator {

    private val CATEGORIES:List<Category> = listOf(
        Category(1,"red","for work"),
        Category(2,"orange","learnings")
    )

    fun generatedCategories():MutableList<Category>{
        return CATEGORIES.toMutableList()
    }

}