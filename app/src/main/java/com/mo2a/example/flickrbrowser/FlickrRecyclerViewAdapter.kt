package com.mo2a.example.flickrbrowser

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso


class FlickrImageViewHolder(view: View): RecyclerView.ViewHolder(view){
    var thumbnail: ImageView= view.findViewById(R.id.thumbnail)
    var title: TextView= view.findViewById(R.id.title)
}

class FlickrRecyclerViewAdapter(private var photoList: List<Photo>): RecyclerView.Adapter<FlickrImageViewHolder>() {
    private val TAG= "ViewAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlickrImageViewHolder {
        Log.d(TAG, "onCreateViewHolder called")
        val view= LayoutInflater.from(parent.context).inflate(R.layout.browse, parent, false)
        return FlickrImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if(photoList.isNotEmpty()) photoList.size else 1
    }

    override fun onBindViewHolder(holder: FlickrImageViewHolder, position: Int) {
        if(photoList.isEmpty()){
            holder.thumbnail.setImageResource(R.drawable.baseline_image_black_48dp)
            holder.title.setText(R.string.empty_photo)
        }else{
            val photoItem= photoList[position]
            Picasso.get()
                .load(photoItem.image)
                .error(R.drawable.baseline_image_black_48dp)
                .placeholder(R.drawable.baseline_image_black_48dp)
                .into(holder.thumbnail)

            holder.title.text= photoItem.title
        }
    }

    fun loadNewData(newPhotos: List<Photo>){
        photoList= newPhotos
        notifyDataSetChanged()
    }

    fun getPhoto( position: Int): Photo?{
        return if(photoList.isNotEmpty()) photoList[position] else null
    }
}