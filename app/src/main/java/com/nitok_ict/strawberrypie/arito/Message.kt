package com.nitok_ict.strawberrypie.arito

class Message{

    var voiceMessage: Int = 0   //音声用のメンバ
    var faceData: MutableList<Face> = mutableListOf()   //表情データ保存用のメンバ

    fun sveToFile(){        //ファイルとして保存する為の関数　TODO 実装する
    }

    fun readFromFile(){     //ファイルから復元する為の関数　TODO 実装する
    }

    fun deleteFile(){       //保存されているファイルを削除する関数　TODO 実装する
    }

    fun sortData(){         //開始時間順にソートする関数
        faceData.sortBy { it.startTime }
    }

}

data class Face(var resID: Int,     //表情のリソースID
                var startTime: Int, //その表情が表示され始める時間
                var endTime: Int)   //その表情が表示され終わる時間