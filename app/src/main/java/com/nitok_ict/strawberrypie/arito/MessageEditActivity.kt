package com.nitok_ict.strawberrypie.arito

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class MessageEditActivity : AppCompatActivity() {
    //ViewModelのインスタンスを取ってくる
    private val viewModel: MessageStoreViewModel by viewModels()

    private lateinit var editRecyclerView: RecyclerView
    private lateinit var faceDataListAdapter: EditDataAdapter
    private lateinit var swipeToDismissTouchHelper: ItemTouchHelper

    private val messageEditListener = object : MessageEditListener {
        override fun onFaceDataEdit(position: Int, resID: Int) {
            viewModel.editResID(position, resID)
            faceDataListAdapter.notifyDataSetChanged()
        }

        override fun onDisplayTimeEdit(position: Int, time: Int) {
            viewModel.editDisplayTime(position, time)
            faceDataListAdapter.notifyDataSetChanged()
        }
    }

    private val itemClickListener = object : ItemClickListener {
        override fun onEditFaceClick(position: Int) {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, SelectFaceFragment(position, messageEditListener))
            fragmentTransaction.commit()
        }

        override fun onEditTimeClick(position: Int) {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, SelectTimeFragment(
                position,
                viewModel.getDisplayTime(position),
                messageEditListener))
            fragmentTransaction.commit()
            Log.d("DEBUG", "onEditFaceClick")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messege_edit)

        viewModel.readFromFile(this)

        editRecyclerView = findViewById(R.id.recyclerview_edit_message)
        val addDataButton: MaterialButton = findViewById(R.id.add_data_button)
        val exitButton: MaterialButton = findViewById(R.id.button_face_edit_exit)
        val doneButton: MaterialButton = findViewById(R.id.button_face_edit_done)

        faceDataListAdapter = EditDataAdapter(viewModel.getFaceData(), itemClickListener)
        editRecyclerView.layoutManager = LinearLayoutManager(this)
        editRecyclerView.adapter = faceDataListAdapter
        swipeToDismissTouchHelper = getSwipeToDismissTouchHelper(faceDataListAdapter)
        swipeToDismissTouchHelper.attachToRecyclerView(editRecyclerView)

        addDataButton.setOnClickListener {
            viewModel.addData()
            faceDataListAdapter.notifyDataSetChanged()
        }
        exitButton.setOnClickListener {
            Intent(this, MessageRecordActivity::class.java).also {intent ->
                startActivity(intent)
            }
        }
        doneButton.setOnClickListener {
            viewModel.saveToFile()
            Intent(this, MainActivity::class.java).also { intent ->
                startActivity(intent)
            }
        }
    }

    private fun getSwipeToDismissTouchHelper(adapter: RecyclerView.Adapter<EditDataAdapter.ViewHolder>) =
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            //スワイプ時に実行
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //データリストからスワイプしたデータを削除
                viewModel.deleteData(viewHolder.adapterPosition)

                //リストからスワイプしたカードを削除
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                adapter.notifyDataSetChanged()
            }

            //スワイプした時の背景を設定
            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                val itemView = viewHolder.itemView
                val background = ColorDrawable()
                background.color = Color.parseColor("#f44336")
                if (dX < 0)
                    background.setBounds(
                        itemView.right + dX.toInt(),
                        itemView.top,
                        itemView.right,
                        itemView.bottom
                    )
                else
                    background.setBounds(
                        itemView.left,
                        itemView.top,
                        itemView.left + dX.toInt(),
                        itemView.bottom
                    )

                background.draw(c)
            }
        })
}