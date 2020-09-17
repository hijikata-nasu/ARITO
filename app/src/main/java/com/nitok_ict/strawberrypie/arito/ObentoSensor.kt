package com.nitok_ict.strawberrypie.arito

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class ObentoSensor(activityContext: AppCompatActivity) {

    val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    val BLUETOOTH_SPP: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    init {
        val REQUEST_ENABLE_BT: Int = 1

        //端末がBluetoothに対応していないときトーストを表示する
        if (bluetoothAdapter == null) {
            val toast = Toast.makeText(activityContext ,R.string.toast_bluetooth_unsupported, Toast.LENGTH_LONG)
            toast.show()
            activityContext.moveTaskToBack(false)
        }
        //Bluetoothがオフになっているとき、オンにしてもらう
        if (bluetoothAdapter?.isEnabled == false) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            activityContext.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }
    }

    fun isConnected():Boolean{
        return true
    }

    fun isBatteryLow():Boolean{
        return false
    }

    fun isDetectLift():Boolean{
        return true
    }
}