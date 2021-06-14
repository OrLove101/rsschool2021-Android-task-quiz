package com.rsschool.quiz

data class QuizItem(
    val title: String,
    val question: String,
    val firstAnswer: String,
    val secondAnswer: String,
    val thirdAnswer: String,
    val fourthAnswer: String,
    val fifthAnswer: String,
    val styleId: Int,
    val statusBarColorId: Int
)