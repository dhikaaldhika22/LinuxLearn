package com.myproject.app.linuxlearn.data.model

import com.google.firebase.database.Exclude

data class SubjectMatterModel(
    @get:Exclude
    var id: String = "",
    val imageUrl: String = "",
    val name: String = "",
    val label: String = "",
    val description: String = "",
    val content: List<ContentModel> = listOf()
)