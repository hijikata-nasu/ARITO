package com.nitok_ict.strawberrypie.arito

import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import java.lang.Thread.sleep
import java.net.URI

class MessagePlayActivity : AppCompatActivity() {
    lateinit var faceImageView: ImageView
    lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_play)

        faceImageView = findViewById(R.id.imageview_face)

        val startButton : View = findViewById(R.id.startBtn)
        val exitButton : View = findViewById(R.id.exitBtn)
        val messageStatus : TextView = findViewById(R.id.messageStatus)

        val message = Message()
        message.readFromFile(this)
        message.setVoiceDir(this)

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
            messageStatus.setText(R.string.playing_message_status)
            Thread {
                try {
                    var cnt = 0
                    while (cnt < message.faceDataList.size) {
                        faceImageView.setImageResource(message.faceDataList[cnt].resID)
                        Log.d("debag", message.faceDataList[cnt].toString())
                        val starttime: Long = message.faceDataList[cnt].startTime.toLong() * 1000  //単位はms
                        val endtime: Long = message.faceDataList[cnt].endTime.toLong() * 1000
                        cnt++
                        sleep(endtime - starttime)
                    }
                } catch (ex: InterruptedException) {
                    Thread.currentThread().interrupt()
                }
            }.start()
        }
    }


    override fun onDestroy() {
        mediaPlayer.release()
        super.onDestroy()
    }
}