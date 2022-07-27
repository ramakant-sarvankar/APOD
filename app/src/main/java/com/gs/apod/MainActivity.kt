package com.gs.apod

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gs.apod.repository.Utils
import com.gs.apod.viewmodels.MainActivityViewModel
import com.gs.apod.viewmodels.MainActivityViewModelFactory

class MainActivity : AppCompatActivity() {
    var mainActivityViewModel: MainActivityViewModel? = null
    var imgImage: ImageView? = null
    var txtTitle: TextView? = null
    var txtExplanation: TextView? = null
    var strDate: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imgImage = findViewById(R.id.imgImage)
        txtTitle = findViewById(R.id.txtTitle)
        txtExplanation = findViewById(R.id.txtExplationation)

        if (intent.hasExtra(PhotoFragment.IMAGE_DATE)) {
            strDate = intent.getStringExtra(PhotoFragment.IMAGE_DATE).toString()
        }

        if (savedInstanceState == null || TextUtils.isEmpty(strDate)) {
            val fm = supportFragmentManager
            val ft = fm.beginTransaction()
            ft.add(R.id.container, PhotoFragment.newInstance(strDate?:""))
            ft.commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add(1, 1, 1, "Favourites")
        menu?.add(1, 2, 2, "Select day")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) {
            val intent = Intent(this, FavouriteActivity::class.java)
            startActivity(intent)
        } else if (item.itemId == 2) {
            Utils.openDatePickerDialog(this, onDateSelected = {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra(PhotoFragment.IMAGE_DATE, it)
                startActivity(intent)
            })
        }
        return super.onOptionsItemSelected(item)
    }
}