package com.nitok_ict.strawberrypie.arito

import android.content.Intent
import android.media.MediaRecorder
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Chronometer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_message_record.*
import java.io.File

class MessageRecordActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_record)
        Log.d("debag", "onCreateは起動しています")
        val chronometer: Chronometer = findViewById(R.id.chronometer)

        startBtn.setOnClickListener(){
            chronometer.setBase(SystemClock.elapsedRealtime()) //タイマーリセット
            chronometer.start()  //タイマー開始
            startMediaRecord()  //録音開始
            Log.d("debag", "録音開始しました")
        }

        stopBtn.setOnClickListener(){
            chronometer.stop()  //タイマー停止
            stopRecord()  //録音終了
            Log.d("debag", "録音停止しました")
        }

        exitBtn.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    //録音
    private var mediarecorder //録音用のメディアレコーダークラス
            : MediaRecorder? = null

    private fun startMediaRecord() {
        val filePath =  applicationContext.filesDir//録音用のファイルパス　FilesDirで作成元アプリの内部ディレクトリを示す
        Log.d("debag", "startMediaRecordは起動しています")
        Log.d("debag", filePath.toString())
        try {
            var mediafile: File = File(filePath, "record.wav")
            if (mediafile.exists()) {
                //ファイルが存在する場合は削除する
                Log.d("debag", "録音したファイルが存在しています")
                mediafile.delete()
            }
            mediarecorder = MediaRecorder()
            //マイクからの音声を録音する
            mediarecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
            //ファイルへの出力フォーマット DEFAULTにするとwavが扱えるはず
            mediarecorder!!.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT)
            //音声のエンコーダーも合わせてdefaultにする
            mediarecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
            //ファイルの保存先を指定
            mediarecorder!!.setOutputFile(mediafile)
            //録音の準備をする
            mediarecorder!!.prepare()
            //録音開始
            mediarecorder!!.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //停止
    private fun stopRecord() {
        Log.d("debag", "stopRecordは起動しています")
        if (mediarecorder == null) {
            Toast.makeText(this@MessageRecordActivity, "mediarecorder = null", Toast.LENGTH_SHORT).show()
        } else {
            try {
                //録音停止
                mediarecorder!!.stop()
                mediarecorder!!.reset()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}