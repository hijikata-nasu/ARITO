package com.nitok_ict.strawberrypie.arito

import android.R.attr.data
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.BufferedReader
import java.io.File

class Message{
    lateinit var voiceMessage: File
    var faceDataList: MutableList<FaceData> = mutableListOf(FaceData(R.drawable.face_image_0, 0, 5))   //表情データ保存用のメンバ

    fun setVoiceDir(context: Context){
        voiceMessage = File(context.filesDir, "message.wav")
    }

    fun saveToFile(context: Context){        //ファイルとして保存する為の関数
        val mapper = jacksonObjectMapper()
        val json = mapper.writeValueAsString(faceDataList)
        File(context.filesDir, "Message.json").writer().use {
            it.write(json)
        }
        Log.d("DEBUG_read", json)
    }

    fun readFromFile(context: Context): Boolean{     //ファイルから復元する為の関数
        val readFile = File(context.filesDir, "Message.json")
        if (readFile.exists()){
            val mapper = jacksonObjectMapper()
            val json = readFile.bufferedReader().use(BufferedReader::readText)
            faceDataList = mapper.readValue<MutableList<FaceData>>(json)

            Log.d("DEBUG_read", faceDataList.toString())

            return true
        } else {
            return false
        }
    }

    //保存されているファイルを削除する関数
    fun deleteFile(context: Context){
        val readFile = File(context.filesDir, "Message.json")
        if (readFile.exists()){
            readFile.delete()
        }
    }

    fun sortData(){         //開始時間順にソートする関数
        faceDataList.sortBy { it.startTime }
    }

    fun isSonzai(context: Context): Boolean{
        val readFile = File(context.filesDir, "Message.json")
        return readFile.exists()
    }

}

data class FaceData(
    var resID: Int,     //表情のリソースID
    var startTime: Int, //その表情が表示され始める時間
    var endTime: Int
)   //その表情が表示され終わる時間