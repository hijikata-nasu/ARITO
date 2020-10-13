package com.nitok_ict.strawberrypie.arito

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Chronometer
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_message_record.*
import java.io.File

class MessageRecordActivity : AppCompatActivity() {
    val message = Message()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_record)

        val recordAudioPermission = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.RECORD_AUDIO)

        if (recordAudioPermission == PackageManager.PERMISSION_GRANTED) {
            //ActivityCompat.requestPermissions(callingActivity, arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_PERMISSON)
            // TODO パーミッションの取得処理を書く
        }
        val chronometer: Chronometer = findViewById(R.id.chronometer)

        startBtn.setOnClickListener{
            chronometer.base = SystemClock.elapsedRealtime() //タイマーリセット
            chronometer.start()  //タイマー開始
            startMediaRecord()  //録音開始
            Log.d("debag", "録音開始しました")
        }

        stopBtn.setOnClickListener{
            chronometer.stop()  //タイマー停止
            stopRecord()  //録音終了
            Log.d("debag", "録音停止しました")
        }

        exitBtn.setOnClickListener{
            Intent(this, MessageEditActivity::class.java).also { intent ->
                startActivity(intent)
            }
        }
    }

    //録音
    // 録音用のメディアレコーダークラス
    private lateinit var mediarecorder: MediaRecorder

    private fun startMediaRecord() {
        message.setVoiceDir(this)
        try {
            val mediaFile: File = message.voiceMessage
            if (mediaFile.exists()) {
                //ファイルが存在する場合は削除する
                Log.d("debag", "録音したファイルが存在しています")
                mediaFile.delete()
            }
            mediarecorder = MediaRecorder()
            //マイクからの音声を録音する
            mediarecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            //ファイルへの出力フォーマット DEFAULTにするとwavが扱えるはず
            mediarecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT)
            //音声のエンコーダーも合わせてdefaultにする
            mediarecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
            //ファイルの保存先を指定
            mediarecorder.setOutputFile(mediaFile)
            //録音の準備をする
            mediarecorder.prepare()
            //録音開始
            mediarecorder.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //停止
    private fun stopRecord() {
        Log.d("debag", "stopRecordは起動しています")
        try {
            //録音停止
            mediarecorder.stop()
            mediarecorder.reset()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}