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

package com.ktvipin

import java.io.Serializable

/**
 * Created by Vipin KT on 28/06/20
 */
data class CameraOptions(
    var mirrorImage: Boolean = true,
    var flashEnabled: Boolean = true,
    var supportFrontCamera: Boolean = true,
    var captureMode: CaptureMode = CaptureMode.MIXED,
    var recordingDuration: Long = 0
) : Serializable {

    enum class CaptureMode {
        /** A mode where image capture is enabled.  */
        IMAGE,

        /** A mode where video capture is enabled.  */
        VIDEO,

        /**
         * A mode where both image capture and video capture are simultaneously enabled. Note that
         * this mode may not be available on every device.
         */
        MIXED
    }
}