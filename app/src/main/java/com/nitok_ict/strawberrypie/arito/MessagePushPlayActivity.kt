package com.nitok_ict.strawberrypie.arito

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri

class MessagePushPlayActivity : AppCompatActivity() {
    private lateinit var faceImage: ImageView
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var message: Message

    private val messagePlayThread = Thread {
        mediaPlayer.start()
        for (potion in 0 until message.faceDataList.size){
            handler.post{faceImage.setImageResource(message.faceDataList[potion].resID)}
            Thread.sleep((message.faceDataList[potion].endTime - message.faceDataList[potion].startTime).toLong() * 1000)
        }
        mediaPlayer.release()
        finishAndRemoveTask()
    }

    private val handler: Handler = object : Handler() {
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_message_push_play)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        this.setShowWhenLocked(true)
        this.setTurnScreenOn(true)
        Log.d("DEBUG", "Activity起動")

        faceImage = findViewById(R.id.imageview_push_play_face_image)
        message = Message(applicationContext)
        message.readFromFile()

        mediaPlayer = MediaPlayer.create(this, message.voiceMessageFile.toUri())
        mediaPlayer.isLooping = false
        mediaPlayer.pause()


    }

    override fun onResume() {
        super.onResume()
        messagePlayThread.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        message.deleteFile()
    }
}