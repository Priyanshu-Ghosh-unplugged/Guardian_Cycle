package com.guardiancycle

import android.os.Build
//import kotlinx.coroutines.flow.internal.NoOpContinuation.context

import java.io.File
import java.util.Date
import java.util.concurrent.LinkedBlockingQueue
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

//import kotlin.coroutines.jvm.CompletedContinuation.context


class ErrorLogger {
    private val errorQueue = LinkedBlockingQueue<ErrorLog>()

    fun logError(error: GuardianError) {
        val errorLog = ErrorLog(
            timestamp = System.currentTimeMillis(),
            error = error,
            deviceInfo = DeviceInfo(),
            stackTrace = error.stackTraceToString()
        )

        errorQueue.offer(errorLog)
        processErrorLog(errorLog)
    }

    private fun processErrorLog(errorLog: ErrorLog) {
        // Local logging
        logToFile(errorLog)

        // If critical error, send to remote
        if (isCriticalError(errorLog.error)) {
            sendToRemoteLogging(errorLog)
        }
    }

    private fun logToFile(errorLog: ErrorLog) {
        val logFile = File(context.getExternalFilesDir(null), "guardian_error.log")
        logFile.appendText(
            """
            ${Date(errorLog.timestamp)}
            Error: ${errorLog.error}
            Device: ${errorLog.deviceInfo}
            Stack Trace:
            ${errorLog.stackTrace}
            
            """.trimIndent()
        )
    }

    data class ErrorLog(
        val timestamp: Long,
        val error: GuardianError,
        val deviceInfo: DeviceInfo,
        val stackTrace: String
    )

    data class DeviceInfo(
        val model: String = Build.MODEL,
        val manufacturer: String = Build.MANUFACTURER,
        val androidVersion: String = Build.VERSION.RELEASE,
        val appVersion: String
    )
}