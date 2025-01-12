package com.guardiancycle

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper

class FakeConversationPlayer(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    private val conversationParts = listOf(
        ConversationPart(R.raw.hello, 0),
        ConversationPart(R.raw.how_are_you, 3000),
        ConversationPart(R.raw.what_doing, 7000),
        ConversationPart(R.raw.ok_talk_later, 15000)
    )

    fun startConversation() {
        conversationParts.forEach { part ->
            Handler(Looper.getMainLooper()).postDelayed({
                playAudioPart(part.resourceId)
            }, part.delay.toLong())
        }
    }

    private fun playAudioPart(resourceId: Int) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(context, resourceId).apply {
            setOnCompletionListener { release() }
            start()
        }
    }

    fun stopConversation() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    data class ConversationPart(
        val resourceId: Int,
        val delay: Int
    )
}
