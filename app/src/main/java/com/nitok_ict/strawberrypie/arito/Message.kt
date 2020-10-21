package com.nitok_ict.strawberrypie.arito

import android.content.Context
import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.BufferedReader
import java.io.File

class Message(context: Context){
    companion object{
        fun isExists(context: Context): Boolean{
            val messageFile = File(context.filesDir, "Message.json")
            val voiceMessage = File(context.filesDir, "Message.wav")
            return (messageFile.exists() && voiceMessage.exists())
        }
    }
    val voiceMessageFile = File(context.filesDir, "Message.wav")
    val faceDataFile = File(context.filesDir, "Message.json")
    var faceDataList: MutableList<FaceData> = mutableListOf(FaceData(R.drawable.face_image_0, 0, 5))   //表情データ保存用のメンバ

    fun saveToFile(){        //ファイルとして保存する為の関数
        val mapper = jacksonObjectMapper()
        val json = mapper.writeValueAsString(faceDataList)
        faceDataFile.writer().use {
            it.write(json)
        }
        Log.d("DEBUG_read", json)
    }

    fun readFromFile(): Boolean{     //ファイルから復元する為の関数
        if (faceDataFile.exists()){
            val mapper = jacksonObjectMapper()
            val json = faceDataFile.bufferedReader().use(BufferedReader::readText)
            faceDataList = mapper.readValue(json)
        } else {
            return false
        }
        return true
    }

    //保存されているファイルを削除する関数
    fun deleteFile(){
        if (faceDataFile.exists()){
            faceDataFile.delete()
        }
        if (voiceMessageFile.exists()){
            voiceMessageFile.delete()
        }
    }
}

data class FaceData(
    var resID: Int,     //表情のリソースID
    var startTime: Int, //その表情が表示され始める時間
    var endTime: Int
)   //その表情が表示され終わる時間