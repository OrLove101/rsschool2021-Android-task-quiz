package com.rsschool.quiz

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rsschool.quiz.databinding.FragmentQuizBinding
import java.text.FieldPosition

class QuizFragment: Fragment() {
    var content: QuizItem? = null
    private var isFirst: Boolean? = null
    private var isLast: Boolean? = null
    private var answers: Array<String?> = arrayOf()


    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setUpTheme()
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFirst = arguments?.getBoolean(IS_FIRST_KEY)
        isLast = arguments?.getBoolean(IS_LAST_KEY)
        quizPosition = arguments?.getInt(QUIZ_POSITION)
        answers = arguments?.getStringArray(QUIZ_ANSWERS)!!

        setUpActionBar()

        setUpNavigateButtons()

        binding.question.text = content?.question

        binding.optionOne.text = content?.firstAnswer
        binding.optionTwo.text = content?.secondAnswer
        binding.optionThree.text = content?.thirdAnswer
        binding.optionFour.text = content?.fourthAnswer
        binding.optionFive.text = content?.fifthAnswer

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            answers[quizPosition ?: 0] = view.findViewById<RadioButton>(checkedId).text.toString()
            binding.nextButton.isEnabled = true
        }
    }

    private fun setUpNavigateButtons() {
        if ( isFirst == true ) {
            binding.previousButton.isEnabled = false
        } else {
            binding.previousButton.setOnClickListener {
                (activity as QuizCommunicator).onPrevQuiz()
            }
        }
        if ( isLast == true ) {
            binding.nextButton.text = "Submit"
            binding.nextButton.setOnClickListener {
                (activity as QuizCommunicator).onSubmitQuiz()
            }
        } else {
            binding.nextButton.setOnClickListener {
                (activity as QuizCommunicator).onNextQuiz()
            }
        }
        binding.nextButton.isEnabled = false
    }

    interface QuizCommunicator {
        fun onNextQuiz()
        fun onSubmitQuiz()
        fun onPrevQuiz()
    }

    private fun setUpActionBar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.apply {
                if ( isFirst == true ) {
                    setDisplayHomeAsUpEnabled(false)
                } else {
                    setDisplayHomeAsUpEnabled(true)
                    setHomeButtonEnabled(true)
                }
            }
            binding.toolbar.setNavigationOnClickListener {
                (activity as QuizCommunicator).onPrevQuiz()
            }
            supportActionBar?.title = content?.title
        }
    }

    private fun setUpTheme() {
        activity?.theme?.applyStyle(content?.styleId ?: 0, true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity?.window?.statusBarColor = activity?.getColor(content?.statusBarColorId ?: 0)
                ?: 0
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    companion object {
        fun newInstance(content: QuizItem, isFirst: Boolean, isLast: Boolean,
                        answers: Array<String?>, quizPosition: Int): QuizFragment {
            val fragment = QuizFragment()
            val args = bundleOf(
                IS_FIRST_KEY to isFirst,
                IS_LAST_KEY to isLast,
                QUIZ_POSITION to quizPosition,
                QUIZ_ANSWERS to answers
            )

            fragment.content = content
            fragment.arguments = args
            return fragment
        }

        private const val IS_FIRST_KEY = "isFirstKey"
        private const val IS_LAST_KEY = "isLastKey"
        private const val QUIZ_POSITION = "quizPosition"
        private const val QUIZ_ANSWERS = "quizAnswers"

        private var quizPosition: Int? = null
    }
}