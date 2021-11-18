package com.example.starclicker.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.starclicker.R
import com.example.starclicker.database.StarClickerDatabase
import com.example.starclicker.databinding.GameFragmentBinding
import com.example.starclicker.dialogs.BoostDialog
import com.example.starclicker.ui.starView.StarView
import com.example.starclicker.ViewModelFactory

class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel
    private lateinit var binding: GameFragmentBinding
    private lateinit var starView: StarView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)

        binding.boostButton.setOnClickListener {
            BoostDialog.newInstance().show(childFragmentManager, "boost")
        }

        val args = GameFragmentArgs.fromBundle(requireArguments())

        val datasource = StarClickerDatabase.getInstance(requireActivity().application).databaseDao
        val viewModelFactory = ViewModelFactory(datasource)

        viewModel = ViewModelProvider(this, viewModelFactory)[GameViewModel::class.java]

        binding.gameViewModel = viewModel
        binding.lifecycleOwner = this

        starView = requireActivity().findViewById(R.id.starView)

        starView.setOnStarClickListener {
            binding.root.findNavController().navigate(
                GameFragmentDirections.actionGameFragmentToGameOverFragment(
                    viewModel.score.value ?: 0,
                    viewModel.time,
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

    override fun onDestroyView() {
        super.onDestroyView()
        starView.setOnSpecialStarClickListener(null)
        starView.setOnStarClickListener(null)
    }
}