package com.nitok_ict.strawberrypie.arito

import android.media.MediaPlayer
import android.os.Handler
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri

class MessagePushPlayActivity : AppCompatActivity() {
    private lateinit var faceImage: ImageView
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var message: Message

    private val handler: Handler = object : Handler() {
        override fun handleMessage(msg: android.os.Message) {
            super.handleMessage(msg)
        }
    }

    override fun onResume() {
        super.onResume()

        this.setShowWhenLocked(true)

        setContentView(R.layout.activity_message_play)
        faceImage = findViewById(R.id.imageview_push_play_face_image)
        message = Message()
        message.readFromFile(this)
        message.setVoiceDir(this)

        mediaPlayer = MediaPlayer.create(this, message.voiceMessage.toUri())
        mediaPlayer.isLooping = false
        mediaPlayer.pause()

        Thread {
            mediaPlayer.start()
            for (potion in 0 until message.faceDataList.size){
                handler.post{faceImage.setImageResource(message.faceDataList[potion].resID)}
                Thread.sleep((message.faceDataList[potion].endTime - message.faceDataList[potion].startTime).toLong() * 1000)
            }
            mediaPlayer.release()
            finishAndRemoveTask()
        }.start()
    }
}