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

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.core.net.toFile
import com.ktvipin.camx.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Vipin KT on 27/06/20
 */
object FileUtils {

    fun getOutputDirectory(context: Context): File {
        val appContext = context.applicationContext
        val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
            File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else appContext.filesDir
    }

    fun getFile(outputDirectory: File, fileExtension: String): File {
        val fileName = SimpleDateFormat(Constants.FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis()) + fileExtension
        return File(outputDirectory, fileName)
    }

    fun scanFile(context: Context, savedUri: Uri) {
        // If the folder selected is an external media directory, this is
        // unnecessary but otherwise other apps will not be able to access our
        // images unless we scan them using [MediaScannerConnection]
        val mimeType = MimeTypeMap.getSingleton()
            .getMimeTypeFromExtension(savedUri.toFile().extension)
        MediaScannerConnection.scanFile(
            context,
            arrayOf(savedUri.toFile().absolutePath),
            arrayOf(mimeType)
        ) { _, uri ->
            //Log.d(TAG, "Image capture scanned into media store: $uri")
        }
    }
}