package com.gs.apod

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.gs.apod.repository.local.tables.Images
import com.gs.apod.viewmodels.MainActivityViewModel
import com.gs.apod.viewmodels.MainActivityViewModelFactory

class FavouriteActivity : AppCompatActivity() {
    private lateinit var  rvFavourites : ViewPager2
    private var mainActivityViewModel: MainActivityViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)
        rvFavourites = findViewById(R.id.pager)


        mainActivityViewModel = ViewModelProvider(
            this,
            MainActivityViewModelFactory(application)
        )[MainActivityViewModel::class.java]
        mainActivityViewModel!!.getAllFavourites().observe(this, Observer {
            val pagerAdapter = ScreenSlidePagerAdapter(this, it)
            rvFavourites.adapter = pagerAdapter
        })
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity, val favouritesList : List<Images>) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = favouritesList.size

        override fun createFragment(position: Int): Fragment = PhotoFragment.newInstance(favouritesList.get(position))
    }
}