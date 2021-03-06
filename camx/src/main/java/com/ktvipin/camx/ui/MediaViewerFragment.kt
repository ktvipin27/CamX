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

package com.ktvipin.camx.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.MediaController
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ktvipin.camx.CamX
import com.ktvipin.camx.R
import com.ktvipin.camx.utils.isVideo
import kotlinx.android.synthetic.main.fragment_media_viewer.*

/**
 * Created by Vipin KT on 27/06/20
 */
class MediaViewerFragment : Fragment(R.layout.fragment_media_viewer) {

    private val args: MediaViewerFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnBack.setOnClickListener { findNavController().popBackStack() }

        val mediaUri = args.mediaUri
        if (mediaUri.isVideo) {
            val mediaController = MediaController(requireContext())
            videoView.setMediaController(mediaController)
            videoView.setVideoURI(mediaUri)
            videoView.seekTo(1)
            videoView.visibility = VISIBLE
            photoView.visibility = GONE
        } else {
            photoView.setImageURI(mediaUri)
            photoView.visibility = VISIBLE
            videoView.visibility = GONE
        }

        btnDone.setOnClickListener {
            with(requireActivity()) {
                setResult(RESULT_OK, Intent().apply {
                    putExtra(CamX.EXTRA_MEDIA, mediaUri)
                })
                finish()
            }
        }

        Handler().postDelayed({
            btnDone.show()
        }, 300)
    }
}