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

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.VideoCapture
import androidx.camera.view.CameraView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ktvipin.CameraOptions
import com.ktvipin.camx.R
import com.ktvipin.camx.controls.ControlView
import com.ktvipin.camx.utils.*
import kotlinx.android.synthetic.main.fragment_camera.*
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by Vipin KT on 27/06/20
 */
class CameraFragment : Fragment(R.layout.fragment_camera), ControlView.Listener {

    private val args: CameraFragmentArgs by navArgs()
    private val options by lazy { args.options }

    private val outputDirectory: File by lazy { FileUtils.getOutputDirectory(requireContext()) }
    private lateinit var cameraExecutor: ExecutorService

    inner class ImageSavedCallback(private val photoFile: File) :
        ImageCapture.OnImageSavedCallback {

        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
            val savedUri = output.savedUri ?: Uri.fromFile(photoFile)
            FileUtils.scanFile(requireContext(), savedUri)

            findNavController()
                .navigate(
                    CameraFragmentDirections.actionCameraFragmentToMediaViewerFragment(
                        savedUri
                    )
                )
        }

        override fun onError(exc: ImageCaptureException) {
            toast("Photo capture failed: ${exc.message}")
        }
    }

    private val videoSavedCallback = object : VideoCapture.OnVideoSavedCallback {

        override fun onVideoSaved(file: File) {
            val savedUri = Uri.fromFile(file)
            FileUtils.scanFile(requireContext(), savedUri)

            findNavController()
                .navigate(
                    CameraFragmentDirections.actionCameraFragmentToMediaViewerFragment(
                        savedUri
                    )
                )
        }

        override fun onError(videoCaptureError: Int, message: String, cause: Throwable?) {
            toast("Video capture failed: ${cause?.message}")
        }
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()
        with(cameraView) {
            bindToLifecycle(viewLifecycleOwner)
            captureMode = CameraView.CaptureMode.MIXED
        }
        with(controlView) {
            setListener(this@CameraFragment)
            cameraToggleVisibility =
                options.supportFrontCamera && cameraView.hasBackCamera && cameraView.hasFrontCamera
            flashVisibility = options.flashEnabled
            videoEnabled = options.captureMode != CameraOptions.CaptureMode.IMAGE
        }
    }

    override fun onResume() {
        super.onResume()
        if (!PermissionFragment.hasPermissions(requireContext()))
            findNavController()
                .navigate(CameraFragmentDirections.actionCameraFragmentToPermissionFragment())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
    }

    override fun toggleCamera() {
        cameraView.toggleCamera()
    }

    override fun toggleFlash(flashMode: ControlView.FlashMode) {
        cameraView.flash = when (controlView.getFlashMode()) {
            ControlView.FlashMode.FLASH_MODE_AUTO -> ImageCapture.FLASH_MODE_AUTO
            ControlView.FlashMode.FLASH_MODE_ON -> ImageCapture.FLASH_MODE_ON
            ControlView.FlashMode.FLASH_MODE_OFF -> ImageCapture.FLASH_MODE_OFF
        }
    }

    override fun capturePhoto() {
        val photoFile = FileUtils.getFile(outputDirectory, Constants.IMAGE_FILE_EXTENSION)
        val metadata = ImageCapture.Metadata().apply {
            // Mirror image when using the front camera
            isReversedHorizontal =
                options.mirrorImage && cameraView.cameraLensFacing == CameraSelector.LENS_FACING_FRONT
        }
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(photoFile)
            .setMetadata(metadata)
            .build()
        cameraView.takePicture(outputOptions, cameraExecutor, ImageSavedCallback(photoFile))
    }

    override fun startVideoCapturing() {
        val videoFile = FileUtils.getFile(outputDirectory, Constants.VIDEO_FILE_EXTENSION)
        cameraView.startRecording(videoFile, cameraExecutor, videoSavedCallback)
        controlView.flashVisibility = false
        controlView.cameraToggleVisibility = false
    }

    override fun stopVideoCapturing() {
        cameraView.stopRecording()
        controlView.flashVisibility = true
        controlView.cameraToggleVisibility = cameraView.hasBackCamera && cameraView.hasFrontCamera
    }
}