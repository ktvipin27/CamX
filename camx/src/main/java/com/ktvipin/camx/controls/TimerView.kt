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

package com.ktvipin.camx.controls

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.SystemClock
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ktvipin.camx.R
import com.ktvipin.camx.utils.AnimationUtils.startBlinkAnimation
import com.ktvipin.camx.utils.px
import com.ktvipin.camx.utils.toDuration


/**
 * Created by Vipin KT on 27/06/20
 */
class TimerView : LinearLayout {

    private var startTime = 0L
    private var timerHandler = Handler()
    private val timerThread = object : Runnable {
        override fun run() {
            tvTimer.text = (SystemClock.uptimeMillis() - startTime).toDuration()
            timerHandler.postDelayed(this, 0)
        }
    }

    private val ivRedDot = ImageView(context).apply {
        layoutParams = LayoutParams(6.px, 6.px).apply {
            setImageResource(R.drawable.ic_red_dot_8)
            setMargins(leftMargin, topMargin, 5.px, bottomMargin)
            gravity = Gravity.CENTER
        }
    }.also { addView(it) }

    private val tvTimer = TextView(context).apply {
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            gravity = Gravity.CENTER
            setTextColor(Color.WHITE)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            text = "00:00"
            setPaddingRelative(8, 5, 8, 5)
        }
    }.also { addView(it) }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        background = context.getDrawable(R.drawable.bg_rounded_corner_gray)
        gravity = Gravity.CENTER
        setPadding(5.px, 2.px, 5.px, 2.px)
    }

    fun startTimer() {
        startTime = SystemClock.uptimeMillis()
        timerHandler.postDelayed(timerThread, 0)
        visibility = View.VISIBLE
        ivRedDot.startBlinkAnimation()
    }

    fun stopTimer() {
        timerHandler.removeCallbacks(timerThread)
        ivRedDot.clearAnimation()
        visibility = View.INVISIBLE
        tvTimer.text = "00:00"
    }
}