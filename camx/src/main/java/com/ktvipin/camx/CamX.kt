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

package com.ktvipin.camx

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.ktvipin.CameraOptions
import com.ktvipin.camx.ui.CameraActivity

/**
 * Created by Vipin KT on 27/06/20
 */
object CamX {
    const val REQUEST_CODE = 54321
    internal const val EXTRA_MEDIA = "media"
    internal const val EXTRA_OPTIONS = "options"

    fun openCamera(activity: Activity, options: CameraOptions = CameraOptions()) = with(activity) {
        startActivityForResult(
            Intent(this, CameraActivity::class.java).apply {
                putExtra(EXTRA_OPTIONS, options)
            }, REQUEST_CODE
        )
    }

    fun openCamera(activity: Activity, options: CameraOptions.() -> Unit) {
        val o = CameraOptions()
        options(o)
        openCamera(activity, o)
    }

    fun getMedia(data: Intent?): Uri? = data?.extras?.get(EXTRA_MEDIA) as Uri?
}