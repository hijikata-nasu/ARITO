package com.nitok_ict.strawberrypie.arito

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
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


        editRecyclerView = findViewById(R.id.recyclerview_edit_message)
        val addDataButton: MaterialButton = findViewById(R.id.add_data_button)

        editRecyclerView.layoutManager = LinearLayoutManager(this)

        faceDataListAdapter = EditDataAdapter(viewModel.getFaceData(), this)
        editRecyclerView.adapter = faceDataListAdapter

        addDataButton.setOnClickListener {
            viewModel.addData()
            faceDataListAdapter.notifyDataSetChanged()
        }
    }

    override fun onFaceDataEdit(position: Int, resID: Int) {
        //TODO
    }

    override fun onDisplayTimeEdit(position: Int, time: Int) {
        viewModel.editDisplayTime(position, time)
        faceDataListAdapter.notifyDataSetChanged()
        //TODO
    }

    override fun onEditFaceClick(position: Int) {
        //TODO
    }

    override fun onEditTimeClick(position: Int) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, SelectTimeFragment(position, this))
        fragmentTransaction.commit()
        Log.d("DEBUG", "onEditFaceClick")
    }
}