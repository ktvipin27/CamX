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

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setPadding
import com.ktvipin.camx.R
import com.ktvipin.camx.utils.AnimationUtils.startRotateAnimation
import com.ktvipin.camx.utils.AnimationUtils.startScaleAnimation
import com.ktvipin.camx.utils.Constants.LONG_PRESS_DELAY_MILLIS
import com.ktvipin.camx.utils.Constants.SCALE_DOWN
import com.ktvipin.camx.utils.Constants.SCALE_UP
import com.ktvipin.camx.utils.px


/**
 * Created by Vipin KT on 27/06/20
 */
class ControlView : LinearLayout {

    interface Listener {
        fun toggleCamera()
        fun toggleFlash(flashMode: FlashMode)
        fun capturePhoto()
        fun startVideoCapturing()
        fun stopVideoCapturing()
    }

    enum class FlashMode {
        FLASH_MODE_AUTO,
        FLASH_MODE_ON,
        FLASH_MODE_OFF
    }

    private var isLongPressed: Boolean = false
    private var flashMode: FlashMode = FlashMode.FLASH_MODE_OFF
    private var listener: Listener? = null

    private val timerView = TimerView(context)
        .apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            visibility = View.INVISIBLE
        }.also { addView(it) }

    private val layoutControls = LinearLayout(context).apply {
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            orientation = HORIZONTAL
            topMargin = 5.px
        }
    }.also { addView(it) }

    private val ivFlash = ImageButton(context).apply {
        layoutParams = LayoutParams(48.px, 48.px).apply {
            gravity = Gravity.CENTER
        }
        setImageResource(R.drawable.ic_baseline_flash_off_24)
        setBackgroundColor(Color.TRANSPARENT)
        setOnClickListener { toggleFlash() }
    }.also { layoutControls.addView(it) }

    private val ivCapture = ImageButton(context).apply {
        layoutParams = LayoutParams(70.px, 70.px).apply {
            setMargins(70.px, 20.px, 70.px, 20.px)
            gravity = Gravity.CENTER
        }
        setImageResource(R.drawable.ic_circle_line_white_70)
        setBackgroundColor(Color.TRANSPARENT)
    }.also {
        isHapticFeedbackEnabled = true
        layoutControls.addView(it)
    }

    private val ivSwitchCam = ImageButton(context).apply {
        layoutParams = LayoutParams(48.px, 48.px).apply {
            gravity = Gravity.CENTER
        }
        setImageResource(R.drawable.ic_baseline_flip_camera_24)
        setBackgroundColor(Color.TRANSPARENT)
        setOnClickListener {
            it.startRotateAnimation()
            listener?.toggleCamera()
        }
    }.also { layoutControls.addView(it) }

    private val tvInfo = TextView(context).apply {
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            gravity = Gravity.CENTER
            topMargin = 5.px
        }
        setTextColor(Color.WHITE)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        text = "Hold for Video, tap for Photo"
    }.also { addView(it) }

    var flashVisibility: Boolean = true
        set(value) {
            field = value
            ivFlash.visibility = if (value) View.VISIBLE else View.INVISIBLE
        }

    var cameraToggleVisibility: Boolean = true
        set(value) {
            field = value
            ivSwitchCam.visibility = if (value) View.VISIBLE else View.INVISIBLE
        }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        setPadding(16.px)
        setBackgroundColor(Color.TRANSPARENT)
        gravity = Gravity.CENTER
        orientation = VERTICAL
        setupCaptureButtonListener()
    }

    fun setListener(listener: Listener?) {
        this.listener = listener
    }

    private fun toggleFlash() {
        when (flashMode) {
            FlashMode.FLASH_MODE_AUTO -> {
                flashMode =
                    FlashMode.FLASH_MODE_OFF
                ivFlash.setImageResource(R.drawable.ic_baseline_flash_off_24)
            }
            FlashMode.FLASH_MODE_ON -> {
                flashMode =
                    FlashMode.FLASH_MODE_AUTO
                ivFlash.setImageResource(R.drawable.ic_baseline_flash_auto_24)
            }
            FlashMode.FLASH_MODE_OFF -> {
                flashMode =
                    FlashMode.FLASH_MODE_ON
                ivFlash.setImageResource(R.drawable.ic_baseline_flash_on_24)
            }
        }
        listener?.toggleFlash(flashMode)
    }

    fun getFlashMode() = flashMode

    @SuppressLint("ClickableViewAccessibility")
    private fun setupCaptureButtonListener() {
        var initialTouchX = 0f
        var initialTouchY = 0f
        val mHandler = Handler()
        val mLongPressed = Runnable {
            isLongPressed = true
            ivCapture.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            timerView.startTimer()
            listener?.startVideoCapturing()
        }
        ivCapture.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    mHandler.postDelayed(
                        mLongPressed,
                        LONG_PRESS_DELAY_MILLIS
                    )
                    ivCapture.setImageResource(R.drawable.ic_circle_red_white_70)
                    v.startScaleAnimation(
                        SCALE_UP,
                        SCALE_UP
                    )
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY
                    return@setOnTouchListener true
                }
                MotionEvent.ACTION_UP -> {
                    timerView.stopTimer()
                    mHandler.removeCallbacks(mLongPressed)
                    v.startScaleAnimation(SCALE_DOWN, SCALE_DOWN) {
                        ivCapture.setImageResource(R.drawable.ic_circle_line_white_70)
                    }
                    val xDiff = initialTouchX - event.rawX
                    val yDiff = initialTouchY - event.rawY
                    if ((kotlin.math.abs(xDiff) < 5) && (kotlin.math.abs(yDiff) < 5)) {
                        if (isLongPressed) {
                            isLongPressed = false
                            ivCapture.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                            listener?.stopVideoCapturing()
                        } else {
                            listener?.capturePhoto()
                        }
                    } else {
                        isLongPressed = false
                        ivCapture.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                        listener?.stopVideoCapturing()
                    }
                    v.performClick()
                    return@setOnTouchListener true
                }
                else -> {
                    return@setOnTouchListener false
                }
            }
        }
    }
}