package com.nitok_ict.strawberrypie.arito

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.media.MediaRecorder
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Chronometer
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_message_record.*
import java.io.File

class MessageRecordActivity : AppCompatActivity() {
    companion object{
        const val REQUEST_PERMISSION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_record)

        val recordAudioPermission = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.RECORD_AUDIO)

        if (recordAudioPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this , arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_PERMISSION)
        }

        var isRecording = false
        val chronometer: Chronometer = findViewById(R.id.chronometer)

        recordButton.setOnClickListener{
            if (isRecording){
                recordButton.backgroundTintList = ColorStateList.valueOf(getColor(R.color.white))
                recordButton.imageTintList = ColorStateList.valueOf(getColor(R.color.red))
                recordButton.setImageResource(R.drawable.ic_baseline_fiber_manual_record_24)
                recordDoneButton.isEnabled = true
                chronometer.stop()  //タイマー停止
                stopRecord()  //録音終了
                Log.d("debag", "録音停止しました")
                isRecording = false
            } else {
                recordButton.backgroundTintList = ColorStateList.valueOf(getColor(R.color.red))
                recordButton.imageTintList = ColorStateList.valueOf(getColor(R.color.white))
                recordButton.setImageResource(R.drawable.ic_baseline_stop_24)
                recordDoneButton.isEnabled = false
                chronometer.base = SystemClock.elapsedRealtime() //タイマーリセット
                chronometer.start()  //タイマー開始
                startMediaRecord()  //録音開始
                Log.d("debag", "録音開始しました")
                isRecording = true

            }
        }
        recordDoneButton.setOnClickListener {
            Intent(this, MessageEditActivity::class.java).also { intent ->
                startActivity(intent)
            }
        }
    }

    //録音
    // 録音用のメディアレコーダークラス
    private lateinit var mediaRecorder: MediaRecorder

    private fun startMediaRecord() {
        val message = Message(this)

        try {
            val mediaFile: File = message.voiceMessageFile
            //ファイルが存在する場合は削除する
            message.deleteFile()
            mediaRecorder = MediaRecorder()
            //マイクからの音声を録音する
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            //ファイルへの出力フォーマット DEFAULTにするとwavが扱えるはず
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT)
            //音声のエンコーダーも合わせてdefaultにする
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
            //ファイルの保存先を指定
            mediaRecorder.setOutputFile(mediaFile)
            //録音の準備をする
            mediaRecorder.prepare()
            //録音開始
            mediaRecorder.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //停止
    private fun stopRecord() {
        try {
            //録音停止
            mediaRecorder.stop()
            mediaRecorder.reset()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}