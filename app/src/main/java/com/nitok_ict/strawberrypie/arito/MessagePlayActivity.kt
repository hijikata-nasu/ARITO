package com.nitok_ict.strawberrypie.arito

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_message_play.*

class MessagePlayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_play)

        val startBtn : Button = findViewById(R.id.startBtn)
        val exitBtn : Button = findViewById(R.id.exitBtn)
        val messageStatus : TextView = findViewById(R.id.messageStatus)

        val messageDate = Message()

        messageDate.readFromFile()

        messageDate.faceData
        messageDate.voiceMessage


        exitBtn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        startBtn.setOnClickListener {
            messageStatus.setText(R.string.playing_message_status)
            //TODO 再生処理
        }
    }
}