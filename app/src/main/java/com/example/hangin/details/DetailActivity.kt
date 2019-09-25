package com.example.hangin.details

import android.os.Bundle
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.hangin.R
import com.example.hangin.place.Place
import com.google.android.material.tabs.TabLayout
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {

    private lateinit var imageViewCover: ImageView
    private lateinit var imageViewPlace: ImageView
    private lateinit var textViewName: TextView
    private lateinit var ratingBarRating: RatingBar
    private lateinit var textViewRating: TextView
    private lateinit var textViewAddress: TextView
    private lateinit var textViewPhone: TextView

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    private lateinit var place: Place
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        imageViewCover = findViewById(R.id.imageView_cover)
        imageViewPlace = findViewById(R.id.imageView_workspaceImage)
        textViewName = findViewById(R.id.textView_workspaceName)
        ratingBarRating = findViewById(R.id.ratingBar_workspaceRating)
        textViewRating = findViewById(R.id.textView_rating)
        textViewAddress = findViewById(R.id.textView_workspaceAddress)
        textViewPhone = findViewById(R.id.textView_workspacePhone)

        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)

        place = intent.getSerializableExtra("place") as Place
    }

    override fun onStart() {
        super.onStart()
        loadWorkspaceData()
    }

    private fun loadWorkspaceData() {
        downloadImage(imageViewCover, place.coverImage)
        downloadImage(imageViewPlace, place.image)
        textViewName.text = place.name
        //ratingBarRating.rating = place.totalRating
        ratingBarRating.rating = 3.5f
        //textViewRating.text = "(${place.totalRating}/5)"
        textViewRating.text = "(3.5/5)"
        textViewAddress.text = place.address
        textViewPhone.text = place.phone

        setupFragment()
    }

    private fun downloadImage(view: ImageView, url: String) = Picasso.get().load(url).placeholder(
        R.drawable.img_placeholder
    ).into(view)

    private fun setupFragment() {
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.review_fragment)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.menu_fragment)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.about_fragment)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.location_fragment)))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = FragmentAdapter(supportFragmentManager, tabLayout.tabCount)
        viewPager.adapter = adapter

        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }
}
