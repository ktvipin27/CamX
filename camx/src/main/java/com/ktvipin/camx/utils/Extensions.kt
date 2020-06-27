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

import android.annotation.SuppressLint
import android.content.res.Resources
import android.net.Uri
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.CameraView
import androidx.fragment.app.Fragment
import com.ktvipin.camx.utils.Constants.RATIO_16_9_VALUE
import com.ktvipin.camx.utils.Constants.RATIO_4_3_VALUE
import java.util.concurrent.TimeUnit

/**
 * Created by Vipin KT on 27/06/20
 */
val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Long.toDuration(): String {
    return String.format("%02d", TimeUnit.MILLISECONDS.toMinutes(this)).plus(
        ":" + String.format(
            "%02d",
            TimeUnit.MILLISECONDS.toSeconds(this) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(this)
            )
        )
    )
}

fun Fragment.toast(message: String) {
    requireActivity().runOnUiThread {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_LONG
        ).show()
    }
}

/** Returns true if the device has an available back camera. False otherwise */
val ProcessCameraProvider?.hasBackCamera: Boolean
    get() = this?.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) ?: false

/** Returns true if the device has an available front camera. False otherwise */
val ProcessCameraProvider?.hasFrontCamera: Boolean
    get() = this?.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) ?: false

/** Returns true if the device has an available back camera. False otherwise */
val CameraView.hasBackCamera: Boolean
    @SuppressLint("MissingPermission")
    get() = hasCameraWithLensFacing(CameraSelector.LENS_FACING_BACK)

/** Returns true if the device has an available front camera. False otherwise */
val CameraView.hasFrontCamera: Boolean
    @SuppressLint("MissingPermission")
    get() = hasCameraWithLensFacing(CameraSelector.LENS_FACING_FRONT)

fun DisplayMetrics.aspectRatio(): Int {
    val previewRatio = kotlin.math.max(widthPixels, heightPixels).toDouble() / kotlin.math.min(
        widthPixels,
        heightPixels
    )
    if (kotlin.math.abs(previewRatio - RATIO_4_3_VALUE) <= kotlin.math.abs(
            previewRatio - RATIO_16_9_VALUE
        )
    ) {
        return androidx.camera.core.AspectRatio.RATIO_4_3
    }
    return androidx.camera.core.AspectRatio.RATIO_16_9
}

val Uri?.isVideo: Boolean
    get() = this?.path?.endsWith(Constants.VIDEO_FILE_EXTENSION, true) ?: false


