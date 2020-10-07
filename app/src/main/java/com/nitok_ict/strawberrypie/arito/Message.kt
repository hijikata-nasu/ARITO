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
    var voiceMessage: Int = 0   //音声用のメンバ
    var faceDataList: MutableList<FaceData> = mutableListOf(FaceData(R.drawable.face_image_0, 0, 5))   //表情データ保存用のメンバ

    fun saveToFile(context: Context){        //ファイルとして保存する為の関数　TODO 実装する
        val mapper = jacksonObjectMapper()
        val json = mapper.writeValueAsString(faceDataList)
        File(context.filesDir, "Message.json").writer().use {
            it.write(json)
        }
        Log.d("DEBUG_read", json)
    }

    fun readFromFile(context: Context): Boolean{     //ファイルから復元する為の関数　TODO 実装する
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

    fun deleteFile(context: Context){       //保存されているファイルを削除する関数　TODO 実装する
        val readFile = File(context.filesDir, "Message.json")
        if (readFile.exists()){
            readFile.delete()
        }
    }

    fun sortData(){         //開始時間順にソートする関数
        faceDataList.sortBy { it.startTime }
    }

}

data class FaceData(
    var resID: Int,     //表情のリソースID
    var startTime: Int, //その表情が表示され始める時間
    var endTime: Int
)   //その表情が表示され終わる時間