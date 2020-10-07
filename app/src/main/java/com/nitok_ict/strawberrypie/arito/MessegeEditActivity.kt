package com.nitok_ict.strawberrypie.arito

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class MessegeEditActivity : AppCompatActivity(), ItemClickListener, MessageEditListener {
    //ViweModelのインスタンスを取ってくる
    val viewModel: MessageStoreViewModel by viewModels()

    lateinit var editRecyclerView: RecyclerView
    lateinit var faceDataListAdapter: EditDataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messege_edit)

        viewModel.readFromFile(this)

        editRecyclerView = findViewById(R.id.recyclerview_edit_message)
        val addDataButton: MaterialButton = findViewById(R.id.add_data_button)
        val exitButton: MaterialButton = findViewById(R.id.button_face_edit_exit)
        val doneButton: MaterialButton = findViewById(R.id.button_face_edit_done)

        faceDataListAdapter = EditDataAdapter(viewModel.getFaceData(), this)
        editRecyclerView.layoutManager = LinearLayoutManager(this)
        editRecyclerView.adapter = faceDataListAdapter

        addDataButton.setOnClickListener {
            viewModel.addData()
            faceDataListAdapter.notifyDataSetChanged()
        }
        exitButton.setOnClickListener {
            //TODO 戻る処理
        }
        doneButton.setOnClickListener {
            viewModel.saveToFile(this)
            Intent(this, MainActivity::class.java).also { intent ->
                startActivity(intent)
            }
        }
    }

    override fun onFaceDataEdit(position: Int, resID: Int) {
        viewModel.editResID(position, resID)
        faceDataListAdapter.notifyDataSetChanged()
    }

    override fun onDisplayTimeEdit(position: Int, time: Int) {
        viewModel.editDisplayTime(position, time)
        faceDataListAdapter.notifyDataSetChanged()
    }

    override fun onEditFaceClick(position: Int) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, SelectFaceFragment(position, this))
        fragmentTransaction.commit()
    }

    override fun onEditTimeClick(position: Int) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, SelectTimeFragment(position, this))
        fragmentTransaction.commit()
        Log.d("DEBUG", "onEditFaceClick")
    }
}