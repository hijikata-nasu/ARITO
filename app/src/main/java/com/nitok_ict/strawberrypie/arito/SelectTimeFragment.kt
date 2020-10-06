package com.nitok_ict.strawberrypie.arito

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton

class SelectTimeFragment(private val position: Int, private val listener: MessageEditListener) : Fragment(){
    var displayTime: Int = 5

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_time, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val timeDownButton: MaterialButton = view.findViewById(R.id.button_time_down)
        val timeUpButton: MaterialButton = view.findViewById(R.id.button_time_up)
        val doneButton: MaterialButton = view.findViewById(R.id.button_done)
        val displayTimeTextviwe: TextView = view.findViewById(R.id.textview_time_display)

        displayTimeTextviwe.text = (displayTime.toString() + "秒")

        timeDownButton.setOnClickListener {
            if (displayTime > 0){
                displayTime -= 1
                displayTimeTextviwe.text = (displayTime.toString() + "秒")
            }
        }
        timeUpButton.setOnClickListener {
            displayTime += 1
            displayTimeTextviwe.text = (displayTime.toString() + "秒")
        }
        doneButton.setOnClickListener {
            listener.onDisplayTimeEdit(position, displayTime)
            parentFragmentManager.beginTransaction().remove(this).commit()
        }
    }
}