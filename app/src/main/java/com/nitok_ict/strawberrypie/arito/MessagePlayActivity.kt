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
    lateinit var faceImageView: ImageView
    lateinit var mediaPlayer: MediaPlayer
    lateinit var message: Message

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_play)

        faceImageView = findViewById(R.id.imageview_face)

        val startButton : View = findViewById(R.id.startBtn)
        val exitButton : View = findViewById(R.id.exitBtn)
        val messageStatus : TextView = findViewById(R.id.messageStatus)

        message = Message()
        message.readFromFile(this)
        message.setVoiceDir(this)

        val filePath = message.voiceMessage.toUri()

        //再生する音声ファイルの設定
        mediaPlayer = MediaPlayer.create(this, filePath )
        mediaPlayer.isLooping = false
        mediaPlayer.pause()

        messageStatus.setText(R.string.stopped_message_status)

        exitButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        startButton.setOnClickListener {
            mediaPlayer.start()  //再生
            startShowImage(message.faceDataList)
        }
    }

    override fun onDestroy() {
        mediaPlayer.release()
        super.onDestroy()
    }

    fun startShowImage(faceDataList: MutableList<FaceData>){
        Thread {
            for (potion in 0 until faceDataList.size){
                handler.post{setImage(potion)}
                Thread.sleep((faceDataList[potion].endTime - faceDataList[potion].startTime).toLong() * 1000)
            }
        }.start()
    }

    private val handler: Handler = object : Handler() {
        override fun handleMessage(msg: android.os.Message) {
            super.handleMessage(msg)
        }
    }

    fun setImage(position: Int){
        faceImageView.setImageResource(message.faceDataList[position].resID)
    }
}