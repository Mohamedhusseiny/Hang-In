package com.example.hangin.place

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.example.hangin.R


class PlaceAdapter(
    private var places: ArrayList<Place>
) : RecyclerView.Adapter<PlaceHolder>() {

    override fun onBindViewHolder(holder: PlaceHolder, position: Int) {
        val item = places[position]
        holder.bindLayout(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceHolder {
        val inflatedView = parent.inflate(R.layout.item_place, false)
        return PlaceHolder(inflatedView, places)
    }

    private fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
    }

    override fun getItemCount() = places.size

    fun clear() {
        places.clear()
    }

    fun filter(name: String): ArrayList<Place> {
        var filteredPlaces = ArrayList<Place>()
        for (place in places) {
            if (place.name.contains(name, ignoreCase = true)) {
                filteredPlaces.add(place)
            }
        }
        return filteredPlaces
    }

    interface RecyclerViewClickListener {
        fun recyclerViewListClicked(v: View, position: Int)
    }
}
