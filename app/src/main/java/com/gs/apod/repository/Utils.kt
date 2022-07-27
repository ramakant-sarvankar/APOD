package com.gs.apod.repository

import android.app.DatePickerDialog
import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.gs.apod.R
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class Utils {
    companion object {
        fun loadImage(img: ImageView, url: String?) {
            val circularProgressDrawable = CircularProgressDrawable(img.context)
            val requestOptions: RequestOptions = RequestOptions()
                .placeholder(circularProgressDrawable)
            circularProgressDrawable.setStrokeWidth(5f)
            circularProgressDrawable.setCenterRadius(30f)
            circularProgressDrawable.start()
            requestOptions.override(Resources.getSystem().getDisplayMetrics().widthPixels)
            requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA)
            Glide.with(img.context)
                .setDefaultRequestOptions(requestOptions)
                .load(url)
                .error(R.drawable.ic_internet_coonection_issue)
                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(img)
        }

        fun openDatePickerDialog(context : Context,  onDateSelected: (String) -> Unit) {
            // Get Current Date
            val cal: Calendar = Calendar.getInstance()

            val datePickerDialog = DatePickerDialog(context,
                { view, year, monthOfYear, dayOfMonth ->
                    onDateSelected(year.toString()+"-"+(monthOfYear + 1)+"-"+dayOfMonth)
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis())
            datePickerDialog.show()
        }

        fun getCurrentDate() : String{
            val timeFormatter = SimpleDateFormat("yyyy-MM-dd")
            val date = Date(System.currentTimeMillis())
            return timeFormatter.format(date)

        }
    }
}