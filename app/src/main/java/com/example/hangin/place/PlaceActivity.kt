package com.example.hangin.place

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.hangin.HomeFragment
import com.example.hangin.ProfileFragment
import com.example.hangin.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class PlaceActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    openFragment(HomeFragment.newInstance())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_more -> {
                    openFragment(ProfileFragment.newInstance())
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place)

        // TODO: Stop reloading when select another item
        bottomNavigation = findViewById(R.id.navigationView)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        bottomNavigation.selectedItemId = R.id.nav_home
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
