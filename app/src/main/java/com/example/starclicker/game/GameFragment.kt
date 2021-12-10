package com.example.starclicker.game

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.starclicker.R
import com.example.starclicker.databinding.GameFragmentBinding
import com.example.starclicker.boosters.Boosters
import com.example.starclicker.boosters.dialog.BoostersDialog
import com.example.starclicker.ui.starView.StarView

class GameFragment : Fragment() {

    private val viewModel: GameViewModel by viewModels()
    private lateinit var binding: GameFragmentBinding
    private lateinit var starView: StarView
    private lateinit var countdownTextView: TextView
    private lateinit var shakeNotificationTextView: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)

        binding.boostButton.setOnClickListener {
            starView.stopStars()
            val boostDialog = BoostersDialog.newInstance(onExit = {starView.startStars()})
            boostDialog.show(childFragmentManager, "boost")
        }

        val args = GameFragmentArgs.fromBundle(requireArguments())

        binding.gameViewModel = viewModel
        binding.lifecycleOwner = this

        starView = requireActivity().findViewById(R.id.starView)

        starView.setOnStarClickListener {
            binding.root.findNavController().navigate(
                GameFragmentDirections.actionGameFragmentToGameOverFragment(
                    viewModel.score.value ?: 0,
                    args.difficultyLevel
                )
            )
        }

        starView.setOnSpecialStarClickListener {
            Toast.makeText(requireContext(),"Yay",Toast.LENGTH_SHORT).show()
        }

        viewModel.randomValues()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        countdownTextView = view.findViewById(R.id.countdownTextView)
        shakeNotificationTextView = view.findViewById(R.id.shakeNotificationTextView)

        starView.clearStars()
        starView.stopStars()
        viewModel.startCountdown { countdown ->
            if(countdown > 0)
                countdownTextView.text = countdown.toString()
            else{
                starView.startStars()

                countdownTextView.text = "Start"
                countdownTextView.animate()
                    .alpha(0f)
                    .setDuration(1000L)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            countdownTextView.visibility = View.GONE
                        }
                    })
            }
        }
    }

    private fun showShakeNotification(){
        shakeNotificationTextView.alpha = 0f
        shakeNotificationTextView.visibility = View.VISIBLE
        shakeNotificationTextView.animate()
            .setDuration(300L)
            .alpha(1f)
    }

    private fun hideShakeNotification(){
        shakeNotificationTextView.alpha = 1f
        shakeNotificationTextView.animate()
            .setDuration(300L)
            .alpha(0f)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    shakeNotificationTextView.visibility = View.GONE
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        starView.setOnSpecialStarClickListener(null)
        starView.setOnStarClickListener(null)

        Boosters.clearBoosters()
    }
}