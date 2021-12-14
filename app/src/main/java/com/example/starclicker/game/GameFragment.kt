package com.example.starclicker.game

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
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
import timber.log.Timber

class GameFragment : Fragment(), SensorEventListener {

    private val viewModel: GameViewModel by viewModels()
    private lateinit var binding: GameFragmentBinding
    private lateinit var starView: StarView
    private lateinit var countdownTextView: TextView
    private lateinit var shakeNotificationTextView: TextView

    private lateinit var sensorManager : SensorManager
    private var accSensor: Sensor? = null

    override fun onSensorChanged(event : SensorEvent?) {
        if(event == null) return
        val verticalAcc = event.values[1]
        viewModel.handleSensorData(verticalAcc)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    override fun onResume() {
        super.onResume()
        accSensor!!.also { acceleration ->
            sensorManager.registerListener(this, acceleration, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = context!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
    }

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

        viewModel.progressBar.observe(this, {
            if(it <= 0){
                binding.root.findNavController().navigate(
                    GameFragmentDirections.actionGameFragmentToGameOverFragment(
                        viewModel.score.value ?: 0,
                        args.difficultyLevel
                    )
                )
            }
        })

        starView.setOnStarClickListener {
            viewModel.addPoints(40)
        }


        starView.setOnSpecialStarClickListener {
            Toast.makeText(requireContext(),"Yay",Toast.LENGTH_SHORT).show()
            showShakeNotification()
            viewModel.startShakingMode(onFinished = {hideShakeNotification()})
        }

        //viewModel.randomValues()

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
                viewModel.decreasePointsOverTime()

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
        shakeNotificationTextView.visibility = View.VISIBLE
        shakeNotificationTextView.alpha = 0f
        shakeNotificationTextView.animate()
            .setDuration(300L)
            .alpha(1f)
            .setListener(null)
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