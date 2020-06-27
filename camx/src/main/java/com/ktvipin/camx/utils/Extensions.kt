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

import android.content.res.Resources
import android.widget.Toast
import androidx.fragment.app.Fragment
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


