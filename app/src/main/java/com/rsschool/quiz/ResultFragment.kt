package com.rsschool.quiz

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rsschool.quiz.databinding.FragmentResultBinding

class ResultFragment: Fragment() {
    private  var _binding:FragmentResultBinding? = null
    private val binding get() = _binding!!

    private val questions = arrayOf("To be, or not to be...", "Tallest skyscraper:",
        "The biggest animal:", "What`s superfluous?", "The Answer to the Ultimate Question of" +
                " Life, the Universe and Everything is:")
    private val rightAnswers = arrayOf("To be", "Burj Khalifa, Dubai", "Antarctic blue whale",
        "MicrosoftTeams", "7")
    private var givenAnswers: Array<String?>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity?.window?.statusBarColor = activity?.getColor(R.color.deep_orange_100_dark)
                ?: 0
        }
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        givenAnswers = arguments?.getStringArray(GIVEN_ANSWERS)

        val rightAnswersCount = givenAnswers?.filter { rightAnswers.contains(it) }?.count() ?: 0
        val percentage = (rightAnswersCount.toDouble() / rightAnswers.count()) * 100

        binding.result.text = "Your result: ${percentage.toInt()} %"
        binding.share.setOnClickListener {
            startShareIntent(percentage.toInt())
        }
        binding.restart.setOnClickListener {
            (activity as ResultFragmentCommunicator).onRestartClicked()
        }
        binding.close.setOnClickListener {
            (activity as ResultFragmentCommunicator).onCloseClicked()
        }
    }

    private fun startShareIntent(percentage: Int) {
        var shareText = "Your result: $percentage %\n\n"

        for ( index in rightAnswers.indices) {
            shareText += "${index+1}) ${questions[index]}\n Your answer: ${givenAnswers?.get(index)}\n\n"
        }

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Complete action using")

        if ( sendIntent.resolveActivity(requireActivity().packageManager) != null ) {
            startActivity(shareIntent)
        }
    }

    interface ResultFragmentCommunicator {
        fun onRestartClicked()
        fun onCloseClicked()
    }

    companion object {
        fun newInstance(givenAnswers: Array<String?>): ResultFragment {
            val fragment = ResultFragment()
            val args = bundleOf(GIVEN_ANSWERS to givenAnswers)

            fragment.arguments = args
            return fragment
        }

        private const val GIVEN_ANSWERS = "givenAnswers"
    }
}