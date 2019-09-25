package com.example.hangin.place

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.hangin.R
import com.example.hangin.details.DetailActivity
import com.squareup.picasso.Picasso

class PlaceHolder(
    var view: View,
    var places: ArrayList<Place>
) : RecyclerView.ViewHolder(view), View.OnClickListener {

    private var imageViewPlace: ImageView = view.findViewById(R.id.imageView_place)
    private var textViewPlace: TextView = view.findViewById(R.id.textView_place)
    private var ratingBar: RatingBar = view.findViewById(R.id.rating_bar)

    init {
        view.setOnClickListener(this)
    }


    fun bindLayout(place: Place) {
        Picasso.get().load(place.image).placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(imageViewPlace)
        ratingBar.rating = place.rating
        textViewPlace.text = place.name
    }

    override fun onClick(p0: View?) {
        p0!!.setBackgroundColor(p0.resources.getColor(android.R.color.white))
        openPlaceDetailActivity(adapterPosition)

    }

    private fun openPlaceDetailActivity(position: Int) {
        val place = places[position]
        val detailIntent = Intent(view.context, DetailActivity::class.java)
        detailIntent.putExtra("place", place)
        startActivity(view.context, detailIntent, null)
    }
}