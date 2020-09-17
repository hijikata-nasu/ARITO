package com.nitok_ict.strawberrypie.arito

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {

    val REQUEST_ENABLE_BT: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //ボタン一式
        val messageRecordButton: MaterialButton = findViewById(R.id.record_message_button)
        val retakingButton: MaterialButton = findViewById(R.id.retaking_button)
        val messagePlayButton: MaterialButton = findViewById(R.id.message_play_button)

        val messageCard: MaterialCardView = findViewById(R.id.card_message)

        val bTConnectionTextView: TextView = findViewById(R.id.textview_BTconnection)
        val batteryLifeTextView: TextView = findViewById(R.id.textview_batterylife)
        val messagePlayTimeTextView: TextView = findViewById(R.id.textview_message_playtime)

        val obentoSensor = ObentoSensor(this)

        if(true/*メッセージが記録されているか*/) { //TODO メッセージが記録されているかの確認の処理
            messageCard.visibility = View.VISIBLE
        messagePlayTimeTextView.text = "null秒"  //TODO 再生時間を取得して格納する

        messagePlayButton.setOnClickListener {
            //TODO 画面遷移処理を書く
        }

        retakingButton.setOnClickListener {
            //TODO 画面遷移処理を書く
        }
        } else {
            messageCard.visibility = View.GONE
        }

        if (obentoSensor.isConnected()) {
            bTConnectionTextView.setText(R.string.card_sensor_bluetooth_connected)
            if (obentoSensor.isBatteryLow()) {
                batteryLifeTextView.setText(R.string.card_sensor_battery_life_low)
            } else {
                batteryLifeTextView.setText(R.string.card_sensor_battery_life_high)
            }
        } else {
            bTConnectionTextView.setText(R.string.card_sensor_bluetooth_unconnected)
        }

        messageRecordButton.setOnClickListener{
            //TODO 画面遷移処理を書く
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK){

            } else {
                moveTaskToBack(true)
            }
        }

    }
}