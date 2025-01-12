package com.guardiancycle

import android.os.Looper
import android.os.SystemClock
import java.util.logging.Handler

abstract class CountUpTimer(private val interval: Long) : Runnable {
    private var base: Long = 0
    private var handler: android.os.Handler = android.os.Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null

    fun start() {
        base = SystemClock.elapsedRealtime()
        handler.post(Runnable {
            val elapsedTime = SystemClock.elapsedRealtime() - base
            onTick(elapsedTime)
            runnable = this
            handler.postDelayed(this, interval)
        }.also { runnable = it })
    }

    fun cancel() {
        runnable?.let { handler.removeCallbacks(it) }
    }

    abstract fun onTick(millisElapsed: Long)
    override fun run() {
        TODO("Not yet implemented")
    }
}
