package com.example.starclicker.gameOver

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.starclicker.R
import com.example.starclicker.database.StarClickerDatabase
import com.example.starclicker.databinding.GameOverFragmentBinding
import com.example.starclicker.ViewModelFactory
import com.example.starclicker.database.Score

class GameOverFragment : Fragment() {
    private lateinit var viewModel: GameOverViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: GameOverFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.game_over_fragment, container, false)

        val args = GameOverFragmentArgs.fromBundle(requireArguments())

        val datasource = StarClickerDatabase.getInstance(requireActivity().application).databaseDao
        val viewModelFactory = ViewModelFactory(datasource)

        viewModel = ViewModelProvider(this, viewModelFactory)[GameOverViewModel::class.java]

        binding.again.setOnClickListener {
            it.findNavController()
                .navigate(GameOverFragmentDirections.actionGameOverFragmentToGameFragment(args.difficultyLevel))
        }

        viewModel.insertScore(Score(points = args.score, time = args.time))
        viewModel.checkDatabase()

        return binding.root
    }

}