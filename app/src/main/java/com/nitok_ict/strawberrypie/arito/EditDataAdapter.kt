package com.nitok_ict.strawberrypie.arito

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EditDataAdapter(private val faceDataList: MutableList<FaceData>, private val clickListener: ItemClickListener): RecyclerView.Adapter<EditDataAdapter.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val faceImage: ImageView
        val startTimeTextView: TextView
        val endTimeTextView: TextView
        val orderNumberTextView: TextView

        init {
            faceImage = view.findViewById(R.id.imageview_face_data)
            startTimeTextView = view.findViewById(R.id.textview_start_time)
            endTimeTextView = view.findViewById(R.id.textview_end_time)
            orderNumberTextView = view.findViewById(R.id.textview_order_number)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_face_data, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val faceData = faceDataList[position]

        Log.d("DEBUG_editDataAdapter", position.toString() + "番目 resID:" + faceData.resID.toString())
        Log.d("DEBUG_editDataAdapter", "startTime:" + faceData.startTime.toString())
        Log.d("DEBUG_editDataAdapter", "endTime:" + faceData.endTime.toString())

        holder.orderNumberTextView.text = (position + 1).toString()
        holder.faceImage.setImageResource(faceData.resID)
        holder.startTimeTextView.text = (faceData.startTime.toString() + "秒")
        holder.endTimeTextView.text = (faceData.endTime.toString() + "秒")

        holder.faceImage.setOnClickListener{
            Log.d("DEBUG", "faceImage")
            clickListener.onEditFaceClick(position)
        }
        holder.startTimeTextView.setOnClickListener{
            Log.d("DEBUG", "startTimeText")
            clickListener.onEditTimeClick(position)
        }
        holder.endTimeTextView.setOnClickListener{
            Log.d("DEBUG", "endTimeText")
            clickListener.onEditTimeClick(position)
        }
    }
    override fun getItemCount() = faceDataList.size
}
