package com.starmakerinteractive.thevoic.anim

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import androidx.core.animation.doOnEnd

object FruitsAnimator {
    private const val NORMAL_SIZE = 1f
    private const val BIG_SIZE = 70f

    private const val VISIBLE = 1f
    private const val INVISIBLE = 0f

    private const val ALPHA = "alpha"
    private const val SCALE_X = "scaleX"
    private const val SCALE_Y = "scaleY"

    private const val ANIM_DURATION = 300L

    fun showImage(image: View, onEnd: () -> Unit = {}): Animator {
        return ObjectAnimator.ofFloat(image, ALPHA, INVISIBLE, VISIBLE).apply {
            doOnEnd { onEnd() }
            duration = ANIM_DURATION
        }
    }

    fun showScaled(image: View, onEnd: () -> Unit = {}): AnimatorSet {
        return AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(image, SCALE_X, BIG_SIZE, NORMAL_SIZE),
                ObjectAnimator.ofFloat(image, SCALE_Y, BIG_SIZE, NORMAL_SIZE)
            )
            duration = ANIM_DURATION
        }
    }

    fun hideImage(image: View): Animator {
        return ObjectAnimator.ofFloat(image, ALPHA, VISIBLE, INVISIBLE).apply {
            duration = ANIM_DURATION
        }
    }

    fun showHideImage(image: View, onEnd: () -> Unit = {}): AnimatorSet {
        return AnimatorSet().apply {
            playSequentially(showImage(image), hideImage(image))
            doOnEnd { onEnd() }
        }
    }
}