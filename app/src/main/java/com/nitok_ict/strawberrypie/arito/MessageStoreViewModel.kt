package com.nitok_ict.strawberrypie.arito

import android.content.Context
import androidx.lifecycle.ViewModel

class MessageStoreViewModel: ViewModel() {
    private val message = Message()

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

    fun saveToFile(context: Context){
        message.deleteFile(context)
        message.saveToFile(context)
    }

    fun readFromFile(context: Context){
        message.readFromFile(context)
    }
}