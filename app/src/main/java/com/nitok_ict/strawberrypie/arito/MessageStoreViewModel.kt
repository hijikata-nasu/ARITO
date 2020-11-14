package com.nitok_ict.strawberrypie.arito

import android.content.Context
import androidx.lifecycle.ViewModel

class MessageStoreViewModel(): ViewModel() {
    private lateinit var message: Message

    fun getFaceData(): MutableList<FaceData>{
        return message.faceDataList
    }

    fun addData(){
        message.faceDataList.add(
            FaceData(
                R.drawable.face_image_0,
                message.faceDataList.last().endTime,
                message.faceDataList.last().endTime + 5))
    }

    fun deleteData(position: Int){
        if (message.faceDataList.size == 1){
            return
        }
        message.faceDataList.removeAt(position)
        if (position == 0){
            message.faceDataList[position].startTime = 0
        } else if(position == message.faceDataList.size - 1) {
            message.faceDataList[position].startTime = message.faceDataList[position - 1].endTime
        }
    }

    fun getDisplayTime(position: Int): Int{
        return message.faceDataList[position].endTime - message.faceDataList[position].startTime
    }

    fun editResID(position: Int, resID: Int){
        message.faceDataList[position].resID = resID
    }

    fun editDisplayTime(position: Int, displayTime: Int){
        message.faceDataList[position].endTime = message.faceDataList[position].startTime + displayTime
        if (position < message.faceDataList.size - 1){
            val nextDisplayTime = message.faceDataList[position + 1].endTime - message.faceDataList[position + 1].startTime
            message.faceDataList[position + 1].startTime = message.faceDataList[position].startTime + displayTime
            editDisplayTime(position + 1, nextDisplayTime)
        }
    }

    fun saveToFile(){
        message.saveToFile()
    }

    fun readFromFile(context: Context){
        message = Message(context)
        message.readFromFile()
    }
}