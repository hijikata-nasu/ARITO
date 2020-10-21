package com.nitok_ict.strawberrypie.arito

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri

class MessagePlayActivity : AppCompatActivity() {
    private lateinit var faceImageView: ImageView
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var message: Message

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_play)

        faceImageView = findViewById(R.id.imageview_face)

        val startButton : View = findViewById(R.id.startBtn)
        message = Message(this)
        message.readFromFile()

        val filePath = message.voiceMessageFile.toUri()

        //再生する音声ファイルの設定
        mediaPlayer = MediaPlayer.create(this, filePath )
        mediaPlayer.isLooping = false

        startButton.setOnClickListener {
            it.isEnabled = false
            mediaPlayer.start()  //再生
            Thread {
                for (potion in 0 until message.faceDataList.size){
                    handler.post{setImage(potion)}
                    Thread.sleep((message.faceDataList[potion].endTime - message.faceDataList[potion].startTime).toLong() * 1000)
                }
                handler.post{faceImageView.setImageDrawable(null)}
                handler.post{it.isEnabled = true}
            }.start()
        }
    }

    override fun onDestroy() {
        mediaPlayer.release()
        super.onDestroy()
    }

    private val handler: Handler = object : Handler() {}

    private fun setImage(position: Int){
        faceImageView.setImageResource(message.faceDataList[position].resID)
    }
}