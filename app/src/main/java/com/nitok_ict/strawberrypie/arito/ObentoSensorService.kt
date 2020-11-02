package com.nitok_ict.strawberrypie.arito

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.io.IOException
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

        const val SERVICE_IS_RUNNING = "ObentoSensorService_is_running"

        fun isRunning(context: Context): Boolean{
            return LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(SERVICE_IS_RUNNING)
            )
        }
    }

    private val localBroadcastManager by lazy { LocalBroadcastManager.getInstance(applicationContext) }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
        }
    }

    private var state: Int = STATE_NONE
    private lateinit var bluetoothSocket: BluetoothSocket   //ソケットの設定
    private var isWaiting = false

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
            val outputStream = bluetoothSocket.outputStream
            val inputStream = bluetoothSocket.inputStream
            val buffer = ByteArray(1024)

            outputStream.write("1".toByteArray())

            val inputByte: Int = inputStream.read(buffer)

            return when (String(buffer, 0, inputByte)) {
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
        //Bluetooth接続が確立されていないときにtrueを返す
        if (state != STATE_CONNECTED)return true

        val outputStream = bluetoothSocket.outputStream
        val inputStream = bluetoothSocket.inputStream
        val buffer = ByteArray(1024)

        outputStream.write("2".toByteArray())

        val inputByte: Int = inputStream.read(buffer)

        return when (String(buffer, 0, inputByte)) {
            "1" -> true
            "2" -> false
            else -> {
                Log.d("DEBUG", "読み込み失敗したンゴ")
                true
            }
        }
    }

    fun bluetoothConnect(bluetoothDevice: BluetoothDevice): Boolean{
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
                return false
            }
        }
        return true
    }

    private fun bluetoothDisconnect(){
        if (state == STATE_CONNECTED) {
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

    fun startWaitLiftEvent(){
        if (!isWaiting){
            isWaiting = true
            Thread(
                Runnable {
                    val inputStream = bluetoothSocket.inputStream
                    val buffer = ByteArray(1024)
                    var inputByte: Int
                    var result: String

                    Thread.sleep(10000)

                    /*
                    do {
                        inputByte = inputStream.read(buffer)
                        result = String(buffer, 0, inputByte)
                    } while (inputByte == -1 && isWaiting)

                    if (result[0] == '!') {*/
                        Log.d("DEBUG_Service", "Activity起動中")
                        Intent(this, MessagePushPlayActivity::class.java).also {intent ->
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        //}
                        bluetoothDisconnect()
                        stopSelf()
                    }
                }
            ).start()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //forgroundService
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val name = "Service"
        val id = "casareal_foreground"
        val notifyDescription = "ARITO Service"

        if (manager.getNotificationChannel(id) == null) {
            val mChannel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
            mChannel.apply {
                description = notifyDescription
            }
            manager.createNotificationChannel(mChannel)
        }

        val notification = NotificationCompat.Builder(this, id).apply {
            setContentTitle("メッセージ再生待機中")
            setContentText("メッセージ再生を待機しています。")
            setSmallIcon(R.drawable.ic_launcher_foreground)
        }.build()

        startForeground(1, notification)

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        localBroadcastManager.registerReceiver(broadcastReceiver, IntentFilter(SERVICE_IS_RUNNING))
    }

    override fun onBind(intent: Intent): IBinder {
        isWaiting = false
        return binder
    }

    override fun onDestroy() {
        localBroadcastManager.unregisterReceiver(broadcastReceiver)
        bluetoothDisconnect()
        super.onDestroy()
    }
}
