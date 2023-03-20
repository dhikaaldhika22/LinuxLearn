package com.myproject.app.linuxlearn.data.model

import com.google.firebase.database.Exclude

data class ExerciseModel(
    @get:Exclude
    var id: String = "",
    val name: String = "",
    val description: String = "",
    val questionLabel: String = "",
    val subjectLabel: String = "",
    val question: String = "",
    val optionA: String = "",
    val optionB: String = "",
    val optionC: String = "",
    val optionD: String = "",
    var answer: Int = 0,
    var answerMulti: String = "",
    var userSelectedAnswer: Int = 0
)
