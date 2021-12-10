package com.example.starclicker.title

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.starclicker.R
import com.example.starclicker.databinding.TitleFragmentBinding
import com.example.starclicker.instructions.dialog.InfoDialog

class TitleFragment : Fragment() {

    private val viewModel: TitleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: TitleFragmentBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.title_fragment, container, false
        )

        binding.starButton.apply {
            ObjectAnimator.ofFloat(this, View.ROTATION, -360f, 0f).apply {
                duration = 10000
                repeatCount = Animation.INFINITE
                interpolator = LinearInterpolator()
            }.start()

            setOnClickListener {
                it.findNavController().navigate(
                    TitleFragmentDirections.actionTitleFragmentToGameFragment(DifficultyLevel.Easy)
                )
            }
        }

        binding.questionButton.setOnClickListener {
            InfoDialog.newInstance().show(parentFragmentManager,"instruction")
        }

        binding.accountButton.setOnClickListener {
            viewModel.showUserMenuDialog(requireContext())
        }

        return binding.root
    }
}

enum class DifficultyLevel {
    Easy, Medium, Hard
}