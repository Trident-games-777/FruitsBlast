package com.starmakerinteractive.thevoic.fruit

import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import com.starmakerinteractive.thevoic.R
import com.starmakerinteractive.thevoic.anim.FruitsAnimator
import com.starmakerinteractive.thevoic.databinding.FruitBlastBinding

class FruitBlast : AppCompatActivity() {
    private lateinit var fruitBinding: FruitBlastBinding
    private val fruits = listOf(
        R.drawable.banana,
        R.drawable.bell,
        R.drawable.cherry,
        R.drawable.pineapple,
        R.drawable.plum,
    )
    private var sequenceImages: List<Int>? = null
    private lateinit var sequenceImageViews: List<ImageView>
    private lateinit var guessImageViews: List<ImageView>
    private lateinit var buttons: List<ImageView>
    private val guessed = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fruitBinding = FruitBlastBinding.inflate(layoutInflater)
        setContentView(fruitBinding.root)

        fruitBinding.tvInfo.text = getString(R.string.try_to_remember_fruits)

        sequenceImageViews = listOf(
            fruitBinding.ivSequence0,
            fruitBinding.ivSequence1,
            fruitBinding.ivSequence2,
            fruitBinding.ivSequence3,
            fruitBinding.ivSequence4
        )
        guessImageViews = listOf(
            fruitBinding.ivGuess0,
            fruitBinding.ivGuess1,
            fruitBinding.ivGuess2,
            fruitBinding.ivGuess3,
            fruitBinding.ivGuess4
        )
        buttons = listOf(
            fruitBinding.iv0.also { it.tag = R.drawable.banana },
            fruitBinding.iv1.also { it.tag = R.drawable.bell },
            fruitBinding.iv2.also { it.tag = R.drawable.cherry },
            fruitBinding.iv3.also { it.tag = R.drawable.pineapple },
            fruitBinding.iv4.also { it.tag = R.drawable.plum }
        )

        fruitBinding.btnNext.setOnClickListener { button ->
            button.isEnabled = false
            startGame()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun startGame() {
        fruitBinding.tvInfo.text = resources.getString(R.string.try_to_remember_fruits)
        sequenceImages = fruits.shuffled()
        buttons.forEach { it.isEnabled = false }
        sequenceImageViews.forEach { it.alpha = 0f }
        guessImageViews.forEach { it.setImageResource(android.R.color.transparent) }
        guessed.clear()

        fruitBinding.ivSequence0.setImageResource(sequenceImages!![0])
        fruitBinding.ivSequence1.setImageResource(sequenceImages!![1])
        fruitBinding.ivSequence2.setImageResource(sequenceImages!![2])
        fruitBinding.ivSequence3.setImageResource(sequenceImages!![3])
        fruitBinding.ivSequence4.setImageResource(sequenceImages!![4])

        AnimatorSet().apply {
            playSequentially(
                FruitsAnimator.showHideImage(fruitBinding.ivSequence0),
                FruitsAnimator.showHideImage(fruitBinding.ivSequence1),
                FruitsAnimator.showHideImage(fruitBinding.ivSequence2),
                FruitsAnimator.showHideImage(fruitBinding.ivSequence3),
                FruitsAnimator.showHideImage(fruitBinding.ivSequence4) {
                    fruitBinding.tvInfo.text = getString(R.string.try_to_repeat_sequence)
                    buttons.forEach { button ->
                        button.isEnabled = true
                        button.setOnClickListener {
                            guessed.add(it.tag as Int)
                            val currentIndex = guessed.lastIndex
                            guessImageViews[currentIndex].setImageResource(it.tag as Int)
                            AnimatorSet().apply {
                                playTogether(
                                    FruitsAnimator.showScaled(guessImageViews[currentIndex]),
                                    FruitsAnimator.showImage(sequenceImageViews[currentIndex])
                                )
                                doOnEnd {
                                    if (guessed.size == sequenceImages?.size) {
                                        fruitBinding.btnNext.isEnabled = true
                                        buttons.forEach { btn -> btn.isEnabled = false }

                                        var count = 0
                                        for (i in guessed.indices) {
                                            if (guessed[i] == sequenceImages!![i]) count++
                                        }
                                        fruitBinding.tvInfo.text = "Guessed $count/5"
                                    }
                                }
                                start()
                            }
                        }
                    }
                }
            )
            start()
        }
    }
}