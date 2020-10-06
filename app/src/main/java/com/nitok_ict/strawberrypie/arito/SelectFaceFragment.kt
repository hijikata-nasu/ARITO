package com.nitok_ict.strawberrypie.arito

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class SelectFaceFragment(private val parentPotion: Int, private val listener: MessageEditListener): Fragment(), SelectFaceImage{
    val faceImageList: List<Int> = listOf(
        R.drawable.face_image_0,
        R.drawable.face_image_1,
        R.drawable.face_image_2,
        R.drawable.face_image_3,
        R.drawable.face_image_4,
        R.drawable.face_image_0,
        R.drawable.face_image_1,
        R.drawable.face_image_2,
        R.drawable.face_image_3,
        R.drawable.face_image_4,
        R.drawable.face_image_0,
        R.drawable.face_image_1,
        R.drawable.face_image_2,
        R.drawable.face_image_3,
        R.drawable.face_image_4,
        R.drawable.face_image_0,
        R.drawable.face_image_1,
        R.drawable.face_image_2,
        R.drawable.face_image_3,
        R.drawable.face_image_4,
        R.drawable.face_image_0,
        R.drawable.face_image_1,
        R.drawable.face_image_2,
        R.drawable.face_image_3,
        R.drawable.face_image_4
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_select_face, container, false )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backButton: MaterialButton = view.findViewById(R.id.button_face_select_close)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerview_edit_face_image)
        val adapter = EditFaceImageAdapter(faceImageList, this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this.context, 5, RecyclerView.VERTICAL, false)

        backButton.setOnClickListener {
            parentFragmentManager.beginTransaction().remove(this).commit()
        }
    }

    override fun onImageSelect(resID: Int) {
        listener.onFaceDataEdit(parentPotion, resID)
        parentFragmentManager.beginTransaction().remove(this).commit()
    }
}
