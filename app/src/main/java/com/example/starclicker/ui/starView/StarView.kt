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
    private val stars : MutableList<View> = mutableListOf()

    init {
        inflate(context, R.layout.star_view,this)

        star = findViewById(R.id.star)

        viewModel.shower({createStar()} , {createSpecialStar()})
    }

    fun setOnStarClickListener(clickListener: OnClickListener?){
        onStarClickListener = clickListener
    }

    fun setOnSpecialStarClickListener(clickListener: OnClickListener?){
        onSpecialStarClickListener = clickListener
    }

    fun startStars(){
        viewModel.start()
        viewModel.shower({createStar()} , {createSpecialStar()})
    }

    fun stopStars(){
        viewModel.stop()
    }

    private fun createSpecialStar(){
        createStar(StarType.SPECIAL)
    }

    fun clearStars(){
        val container = star.parent as ViewGroup
        stars.forEach{container.removeView(it)}
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

            scaleX = viewModel.starSizeBoosterModifier() + 1
            scaleY = scaleX

            starW *= scaleX
            starH *= scaleY

            translationX = viewModel.starSpawnWidthBoosterModifier() * container.width

            setOnClickListener {
                when(starType){
                    StarType.NORMAL -> onStarClickListener?.onClick(it)
                    StarType.SPECIAL -> onSpecialStarClickListener?.onClick(it)
                }
            }
        }

        container.addView(newStar)
        stars.add(newStar)

        val mover = ObjectAnimator.ofFloat(
            newStar, View.TRANSLATION_Y,
            -starH, container.height + starH
        ).apply { interpolator = AccelerateInterpolator(viewModel.starSpeedBoosterModifier()) }

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