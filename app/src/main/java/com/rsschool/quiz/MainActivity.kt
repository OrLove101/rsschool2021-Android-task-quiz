package com.rsschool.quiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.FragmentManager
import com.rsschool.quiz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), QuizFragment.QuizCommunicator,
    ResultFragment.ResultFragmentCommunicator {
    private val mAnswers = arrayOfNulls<String>(5)
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if ( savedInstanceState == null ) {
            startQuiz()
        }
    }

    private fun startQuiz() {
        questionsAnswered = 0

        fragments.clear()

        setUpQuizContent()

        openQuiz(fragmentIndex = 0)
    }

    private fun setUpQuizContent() {
        val titles = arrayOf("Question 1", "Question 2", "Question 3", "Question 4", "Question 5")
        val questions = arrayOf("To be, or not to be...", "Tallest skyscraper:",
            "The biggest animal:", "What`s superfluous?", "The Answer to the Ultimate Question of" +
                    " Life, the Universe and Everything is:")
        val firstAnswer = arrayOf("To be", "Shanghai Tower, Shanghai", "Cat", "Discord", "33")
        val secondAnswer = arrayOf("Not to be", "Burj Khalifa, Dubai", "Elephant", "Slack", "11")
        val thirdAnswer = arrayOf("I don`t know", "Trump International Hotel, Chicago",
            "Kitt`s hog-nosed bat", "Kotlin", "7")
        val fourthAnswer = arrayOf("It`s not my problem", "Central Park Tower, New York",
            "Antarctic blue whale", "MicrosoftTeams", "0")
        val fifthAnswer = arrayOf("...that is the question", "Eiffel Tower, Paris", "Human",
            "Telegram", "42")
        val fragmentStyles = arrayOf(R.style.Theme_Quiz_First, R.style.Theme_Quiz_Second,
            R.style.Theme_Quiz_Third, R.style.Theme_Quiz_Fourth, R.style.Theme_Quiz_Fifth)
        val statusBarColors = arrayOf(R.color.deep_orange_100_dark, R.color.yellow_100_dark,
            R.color.third_status_bar_color, R.color.fourth_status_bar_color,
            R.color.fifth_status_bar_color)

        val firstQuizItem = QuizItem(
            titles[0],
            questions[0],
            firstAnswer[0],
            secondAnswer[0],
            thirdAnswer[0],
            fourthAnswer[0],
            fifthAnswer[0],
            fragmentStyles[0],
            statusBarColors[0]
        )
        val firstQuizFragment = QuizFragment.newInstance(firstQuizItem,
            isFirst = true,
            isLast = false,
            mAnswers,
            0
        )

        fragments.add(firstQuizFragment)
        for ( index in 1 until titles.lastIndex ) {
            val quizItem = QuizItem(
                titles[index],
                questions[index],
                firstAnswer[index],
                secondAnswer[index],
                thirdAnswer[index],
                fourthAnswer[index],
                fifthAnswer[index],
                fragmentStyles[index],
                statusBarColors[index]
            )
            val quizFragment = QuizFragment.newInstance(quizItem, isFirst = false, isLast = false,
                mAnswers, index)

            fragments.add(quizFragment)
        }
        val lastQuizItem = QuizItem(
            titles[titles.lastIndex],
            questions[titles.lastIndex],
            firstAnswer[titles.lastIndex],
            secondAnswer[titles.lastIndex],
            thirdAnswer[titles.lastIndex],
            fourthAnswer[titles.lastIndex],
            fifthAnswer[titles.lastIndex],
            fragmentStyles[titles.lastIndex],
            statusBarColors[titles.lastIndex]
        )
        val lastQuizFragment = QuizFragment.newInstance(lastQuizItem,
            isFirst = false,
            isLast = true,
            mAnswers,
            titles.lastIndex
        )

        fragments.add(lastQuizFragment)
    }

    private fun openQuiz(fragmentIndex: Int) {
        if ( fragmentIndex == 0 ) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragments[0])
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragments[fragmentIndex])
                .addToBackStack(null)
                .commit()
        }
    }

    private fun openResultFragment() {
        supportFragmentManager.popBackStack(0, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, ResultFragment.newInstance(mAnswers))
            .commit()
    }

    companion object {
        private val fragments = ArrayList<QuizFragment>()
        private var questionsAnswered = 0
    }

    override fun onBackPressed() {
        fragments.forEach { if ( it.isAdded ) questionsAnswered-- }
        super.onBackPressed()
    }

    override fun onNextQuiz() {
        questionsAnswered++
        openQuiz(questionsAnswered)
    }

    override fun onSubmitQuiz() {
        openResultFragment()
    }

    override fun onPrevQuiz() {
        onBackPressed()
    }

    override fun onRestartClicked() {
        startQuiz()
    }

    override fun onCloseClicked() {
        finish()
    }
}