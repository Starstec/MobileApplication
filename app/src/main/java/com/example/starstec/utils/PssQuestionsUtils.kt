package com.example.starstec.utils

object PssQuestionsUtils {
    data class Question(
        val questionText: String,
        val answers: List<Pair<String, Int>>, // Pair<Answer Text, Points>
    )

    val questions = listOf(
        Question(
            "How physically tense or relaxed do you feel after the match?",
            listOf(
                "Very tense" to 4,
                "Somewhat tense" to 3,
                "Neutral" to 2,
                "Somewhat relaxed" to 1,
                "Very relaxed" to 0
            )
        ),
        Question(
            "How mentally stressed or calm do you feel after the match?",
            listOf(
                "Very stressed" to 4,
                "Somewhat stressed" to 3,
                "Neutral" to 2,
                "Somewhat calm" to 1,
                "Very calm" to 0
            )
        ),
        Question(
            "How emotionally drained or elated do you feel after the match?",
            listOf(
                "Very drained" to 4,
                "Somewhat drained" to 3,
                "Neutral" to 2,
                "Somewhat elated" to 1,
                "Very elated" to 0
            )
        ),
        Question(
            "How confident do you feel about your performance in the match?",
            listOf(
                "Not at all confident" to 4,
                "Slightly confident" to 3,
                "Moderately confident" to 2,
                "Quite confident" to 1,
                "Extremely confident" to 0
            )
        ),
        Question(
            "How well do you think you handled the pressure during the match?",
            listOf(
                "Very poorly" to 4,
                "Somewhat poorly" to 3,
                "Neutral" to 2,
                "Somewhat well" to 1,
                "Very well" to 0
            )
        ),
    )
}
