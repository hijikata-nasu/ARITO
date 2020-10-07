package com.nitok_ict.strawberrypie.arito

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class ObentoSensorService : Service() {
    //このServiceのBinder
    private val binder = ObentoSensorBinder()
    //MainActivity内のBinderに使用されるクラス
    inner class ObentoSensorBinder : Binder() {
        fun getService(): ObentoSensorService = this@ObentoSensorService
    }

    companion object {
        //Bluetooth UUID
        private val UUID_SPP: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

        //Bluetoothの状態を表すための定数
        const val MESSAGE_STATECHANGE = 1
        const val STATE_NONE = 0
        const val STATE_CONNECT_START = 1
        const val STATE_CONNECT_FAILED = 2
        const val STATE_CONNECTED = 3
        const val STATE_CONNECTION_LOST = 4
        const val STATE_DISCONNECT_START = 5
        const val STATE_DISCONNECTED = 6
    }

    private var state: Int = STATE_NONE
    private lateinit var bluetoothSocket: BluetoothSocket   //ソケットの設定

    private var outputStream: OutputStream? = null
    private var inputStream: InputStream? = null
    private val buffer: ByteArray = ByteArray(1024)

    var handler: Handler? = null

    /*デバイスの電池残量を調べる
    * 通信失敗した場合 true
    * 規定値以下の場合 true
    * 規定値以上の場合 falseを返す*/
    fun isBatteryLow():Boolean{
        Log.d("DEBUG", "isBatteryLow呼び出し中")

        if (state != STATE_CONNECTED){
            return true
        }
        else {
            outputStream = bluetoothSocket.outputStream
            inputStream = bluetoothSocket.inputStream

            outputStream?.write("1".toByteArray())

            Log.d("DEBUG", inputStream.toString())

            val inputByte: Int = inputStream!!.read(buffer)
            val result = String(buffer, 0, inputByte)
            Log.d("DEBUG", "通信成功")

            Log.d("DEBUG", result)

            return when (result) {
                "L" -> {
                    Log.d("DEBUG", "バッテリー残量低いらしい")
                    true
                }
                "H" -> {
                    Log.d("DEBUG", "バッテリー残量高いらしい")
                    false
                }
                else -> {
                    Log.d("DEBUG", "読み込み失敗したンゴ")
                    true
                }
            }
        }
    }

    /*お弁当箱がデバイスの上に載っているかを調べる
    * お弁当箱が載っていないときは true  を
    * お弁当箱が載っているときは　 false を返す
    * デバイスとの通信が失敗した時true を返す*/
    fun isDetectLift():Boolean{

        if (state == STATE_CONNECTED)return true

        outputStream = bluetoothSocket.outputStream
        inputStream = bluetoothSocket.inputStream

        outputStream?.write("2".toByteArray())

        val inputByte: Int = inputStream!!.read(buffer)
        val result = String(buffer, 0, inputByte)
        Log.d("DEBUG", "通信成功")

        Log.d("DEBUG", result)

        return when (result) {
            "1" -> {
                Log.d("DEBUG", "お弁当持ち上げられてるらしい")
                true
            }
            "2" -> {
                Log.d("DEBUG", "お弁当持ち上げられてないらしい")
                false
            }
            else -> {
                Log.d("DEBUG", "読み込み失敗したンゴ")
                true
            }
        }
    }

    fun bluetoothConnect(bluetoothDevice: BluetoothDevice): Boolean{
        if (state == STATE_CONNECTED) return true
        try {
            state = STATE_CONNECT_START
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(UUID_SPP)
            state = STATE_CONNECTED
        } catch(e: IOException) {
            state = STATE_CONNECT_FAILED
            return false
        }
        if (state == STATE_CONNECTED){
            try {
                bluetoothSocket.connect()
                state = STATE_CONNECTED
            } catch (connectException: IOException) {
                try {
                    bluetoothSocket.close()
                    state = STATE_CONNECT_FAILED
                } catch (closeException: IOException) {
                    state = STATE_CONNECT_FAILED
                    return false
                }
            }
        }
        return true
    }

    fun bluetoothDisconnect(){
        if (state != STATE_DISCONNECTED) {
            try {
                state = STATE_DISCONNECT_START
                bluetoothSocket.close()
                state = STATE_DISCONNECTED
            } catch (closeException: IOException){
                state = STATE_NONE
            }
        }
        return
    }

    fun isConnected():Boolean = state == STATE_CONNECTED

    fun startEventListener(){
        //ここに再生画面を起動するコードを書く TODO
        Thread(
            Runnable {
                (0..15).map {
                    Thread.sleep(1000)
                }
                stopSelf()
            }
        ).start()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val name = "うんちぷりぷり"
        val id = "casareal_foreground"
        val notifyDescription = "The UNTI is oshiri kara deru yatu"

        if (manager.getNotificationChannel(id) == null) {
            val mChannel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
            mChannel.apply {
                description = notifyDescription
            }
            manager.createNotificationChannel(mChannel)
        }

        val notification = NotificationCompat.Builder(this, id).apply {
            setContentTitle("通知タイトル")
            setContentText("通知の内容")
            setSmallIcon(R.drawable.face_image_0)
        }.build()

        startForeground(1, notification)

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        bluetoothDisconnect()
    }
}
