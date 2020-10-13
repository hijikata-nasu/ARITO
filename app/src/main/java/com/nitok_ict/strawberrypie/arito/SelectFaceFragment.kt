package com.nitok_ict.strawberrypie.arito

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class SelectFaceFragment(private val parentPotion: Int, private val listener: MessageEditListener): Fragment() {
    private val faceImageList: List<Int> = listOf(
        R.drawable.face_image_0,
        R.drawable.face_image_1,
        R.drawable.face_image_2,
        R.drawable.face_image_3,
        R.drawable.face_image_4,
        R.drawable.face_image_5,
        R.drawable.face_image_6,
        R.drawable.face_image_7,
        R.drawable.face_image_8,
        R.drawable.face_image_9,
        R.drawable.face_image_10,
        R.drawable.face_image_11,
        R.drawable.face_image_12,
        R.drawable.face_image_13,
        R.drawable.face_image_14,
        R.drawable.face_image_15,
        R.drawable.face_image_16,
        R.drawable.face_image_17,
        R.drawable.face_image_18,
        R.drawable.face_image_19,
        R.drawable.face_image_20,
        R.drawable.face_image_21,
        R.drawable.face_image_22,
        R.drawable.face_image_23,
        R.drawable.face_image_24,
        R.drawable.face_image_25,
        R.drawable.face_image_26,
        R.drawable.face_image_27,
        R.drawable.face_image_28,
        R.drawable.face_image_29,
        R.drawable.face_image_30,
        R.drawable.face_image_31,
        R.drawable.face_image_32,
        R.drawable.face_image_33,
        R.drawable.face_image_34,
        R.drawable.face_image_35,
        R.drawable.face_image_36,
        R.drawable.face_image_37,
        R.drawable.face_image_38,
        R.drawable.face_image_39,
        R.drawable.face_image_40,
        R.drawable.face_image_41,
        R.drawable.face_image_42,
        R.drawable.face_image_43,
        R.drawable.face_image_44,
        R.drawable.face_image_45,
        R.drawable.face_image_46,
        R.drawable.face_image_47,
        R.drawable.face_image_48,
        R.drawable.face_image_49,
        R.drawable.face_image_50,
        R.drawable.face_image_51,
        R.drawable.face_image_52,
        R.drawable.face_image_53,
        R.drawable.face_image_54,
        R.drawable.face_image_55,
        R.drawable.face_image_56,
        R.drawable.face_image_57,
        R.drawable.face_image_58,
        R.drawable.face_image_59,
        R.drawable.face_image_60,
        R.drawable.face_image_61,
        R.drawable.face_image_62,
        R.drawable.face_image_63,
        R.drawable.face_image_64,
        R.drawable.face_image_65,
        R.drawable.face_image_66,
        R.drawable.face_image_67,
        R.drawable.face_image_68,
        R.drawable.face_image_69,
        R.drawable.face_image_70,
        R.drawable.face_image_71,
        R.drawable.face_image_72,
        R.drawable.face_image_73,
        R.drawable.face_image_74,
        R.drawable.face_image_75,
        R.drawable.face_image_76,
        R.drawable.face_image_77,
        R.drawable.face_image_78,
        R.drawable.face_image_79,
        R.drawable.face_image_80,
        R.drawable.face_image_81,
        R.drawable.face_image_82,
        R.drawable.face_image_83,
        R.drawable.face_image_84,
        R.drawable.face_image_85,
        R.drawable.face_image_86,
        R.drawable.face_image_87,
        R.drawable.face_image_88,
        R.drawable.face_image_89,
        R.drawable.face_image_90,
        R.drawable.face_image_91,
        R.drawable.face_image_92,
        R.drawable.face_image_93,
        R.drawable.face_image_94
    )

    private val selectFaceImage = object : SelectFaceImage {
        override fun onImageSelect(resID: Int) {
            listener.onFaceDataEdit(parentPotion, resID)
            parentFragmentManager.beginTransaction().remove(this@SelectFaceFragment).commit()
        }
    }

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
        val adapter = EditFaceImageAdapter(faceImageList, selectFaceImage)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this.context, 5, RecyclerView.VERTICAL, false)

        backButton.setOnClickListener {
            parentFragmentManager.beginTransaction().remove(this).commit()
        }
    }
}
