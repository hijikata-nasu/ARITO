package com.nitok_ict.strawberrypie.arito

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class EditFaceImageAdapter(private val faceImageResIDList: List<Int>, private val listener: SelectFaceImage): RecyclerView.Adapter<EditFaceImageAdapter.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val faceImage: ImageView = view.findViewById(R.id.imageview_face_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_face_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.faceImage.setImageResource(faceImageResIDList[position])
        holder.faceImage.setOnClickListener {
            listener.onImageSelect(faceImageResIDList[position])
        }
    }

    override fun getItemCount() = faceImageResIDList.size
}