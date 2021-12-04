package com.example.starclicker.ui.starView

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.starclicker.R

class StarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {
    private val star: ImageView
    private val viewModel: StarViewModel =
        ViewModelProvider(context as ViewModelStoreOwner)[StarViewModel::class.java]

    private var onStarClickListener: OnClickListener? = null
    private var onSpecialStarClickListener: OnClickListener? = null

    init {
        inflate(context, R.layout.star_view,this)

        star = findViewById(R.id.star)

        viewModel.shower { createStar() }
    }

    fun setOnStarClickListener(clickListener: OnClickListener?){
        onStarClickListener = clickListener
    }

    fun setOnSpecialStarClickListener(clickListener: OnClickListener?){
        onSpecialStarClickListener = clickListener
    }

    fun startStars(){
        viewModel.start()
        viewModel.shower { createStar() }
    }

    fun stopStars(){
        viewModel.stop()
    }

    fun setStarDelay(value: Long){
        viewModel.setStarDelay(value)
    }

    fun createSpecialStar(){
        createStar(StarType.SPECIAL)
    }

    // todo remove star from view
    private fun createStar(starType: StarType = StarType.NORMAL) {
        val container = star.parent as ViewGroup
        var starW: Float = star.width.toFloat()
        var starH: Float = star.height.toFloat()

        val newStar = AppCompatImageView(context).apply {
            setImageResource(
                when(starType){
                    StarType.NORMAL -> R.drawable.ic_baseline_star_rate_24
                    StarType.SPECIAL -> R.drawable.ic_baseline_special_star_24
                }
            )
            layoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            )

            scaleX = Math.random().toFloat() * 3.5f + 1f
            scaleY = scaleX

            starW *= scaleX
            starH *= scaleY

            translationX = Math.random().toFloat() *
                    container.width - starW / 2

            setOnClickListener {
                when(starType){
                    StarType.NORMAL -> onStarClickListener?.onClick(it)
                    StarType.SPECIAL -> onSpecialStarClickListener?.onClick(it)
                }
            }
        }

        container.addView(newStar)

        val mover = ObjectAnimator.ofFloat(
            newStar, View.TRANSLATION_Y,
            -starH, container.height + starH
        ).apply { interpolator = AccelerateInterpolator(1f) }

        val rotator = ObjectAnimator.ofFloat(
            newStar, View.ROTATION,
            (Math.random() * 1080).toFloat()
        ).apply { interpolator = LinearInterpolator() }

        AnimatorSet().apply {
            playTogether(mover, rotator)
            duration = (Math.random() * 1500 + 500).toLong()

            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    container.removeView(newStar)
                }
            })
        }.start()
    }

    private enum class StarType{
        NORMAL, SPECIAL
    }
}