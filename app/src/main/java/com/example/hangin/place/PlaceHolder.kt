package com.example.hangin.place

import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hangin.R
import com.squareup.picasso.Picasso

class PlaceHolder(view: View) : RecyclerView.ViewHolder(view) {
    private var imageViewPlace: ImageView = view.findViewById(R.id.imageView_place)
    private var textViewPlace: TextView = view.findViewById(R.id.textView_place)
    private var ratingBar: RatingBar = view.findViewById(R.id.rating_bar)

    fun bindLayout(place: Place) {
        Picasso.get().load(place.image).placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(imageViewPlace)
        ratingBar.rating = place.rating
        textViewPlace.text = place.name
    }
}