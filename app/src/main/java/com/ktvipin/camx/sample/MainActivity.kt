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

package com.ktvipin.camx.sample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import com.ktvipin.camx.CamX
import com.ktvipin.camx.utils.isVideo
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnOpenCamera.setOnClickListener { CamX.openCamera(this) }

        val mediaController = MediaController(this)
        videoView.setMediaController(mediaController)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CamX.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                CamX.getMedia(data)?.let {
                    if (it.isVideo) {
                        videoView.setVideoURI(it)
                        videoView.seekTo(1)
                        videoView.visibility = VISIBLE
                        photoView.visibility = GONE
                    } else {
                        photoView.setImageURI(it)
                        photoView.visibility = VISIBLE
                        videoView.visibility = GONE
                    }
                }
            } else {
                photoView.visibility = GONE
                videoView.visibility = GONE
            }
        }
    }
}