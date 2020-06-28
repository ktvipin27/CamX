/*
 * Copyright 2020 Vipin KT
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.ktvipin.camx.utils

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation

/**
 * Created by Vipin KT on 27/06/20
 */
object AnimationUtils {
    fun View.startScaleAnimation(
        scaleX: Float,
        scaleY: Float,
        onAnimationEnd: (() -> Unit)? = null
    ) {
        val scaleDownX = ObjectAnimator.ofFloat(
            this, "scaleX", scaleX
        )
        val scaleDownY = ObjectAnimator.ofFloat(
            this, "scaleY", scaleY
        )
        scaleDownX.duration = 300
        val scaleDown2 = AnimatorSet()
        scaleDown2.play(scaleDownX).with(scaleDownY)
        scaleDown2.start()
        scaleDown2.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                onAnimationEnd?.invoke()
            }

            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationCancel(animation: Animator?) {}
        })
    }

    fun View.startBlinkAnimation() {
        AlphaAnimation(0.0f, 1.0f)
            .apply {
                duration = 500
                startOffset = 20
                repeatMode = Animation.REVERSE
                repeatCount = Animation.INFINITE
            }.also {
                startAnimation(it)
            }
    }

    fun View.startRotateAnimation() {
        val start = if (rotationY == 0f || rotationY == 360f) 0f else 1f
        val end = if (rotationY == 0f || rotationY == 360f) 1f else 0f
        ValueAnimator
            .ofFloat(start, end)
            .apply {
                duration = 500
                addUpdateListener { pAnimation ->
                    val value = pAnimation.animatedValue as Float
                    rotationY = 180 * value
                }
            }
            .start()
    }
}