package com.nitok_ict.strawberrypie.arito

class Message{

    var voiceMessage: Int = 0
    var faceData: MutableList<Face> = mutableListOf()

    fun sveToFile(){
    }

    fun readFromFile(){
    }

    fun deleteFile(){
    }

    fun sortData(){
        faceData.sortBy { it.startTime }
    }

}

data class Face(var resID: Int,
                var startTime: Int,
                var endTime: Int)