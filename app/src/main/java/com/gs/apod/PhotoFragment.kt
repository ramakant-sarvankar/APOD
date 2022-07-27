package com.gs.apod

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gs.apod.repository.Utils
import com.gs.apod.repository.local.tables.Images
import com.gs.apod.viewmodels.MainActivityViewModel
import com.gs.apod.viewmodels.MainActivityViewModelFactory

class PhotoFragment : Fragment() {

    companion object {
        public const val IMAGE_DATE = "image_date"
        public const val IMAGE_DATA = "image_data"
        fun newInstance(date: String) = PhotoFragment().apply {
            arguments = Bundle().apply {
                putString(IMAGE_DATE, date)
            }
        }

        fun newInstance(data: Images) = PhotoFragment().apply {
            arguments = Bundle().apply {
                putSerializable(IMAGE_DATA, data)
            }
        }
    }

    var mainActivityViewModel: MainActivityViewModel? = null
    var imgImage: ImageView? = null
    var txtTitle: TextView? = null
    var txtExplanation: TextView? = null
    var imgFavourite: ImageButton? = null
    var strDate: String? = null
    var isFavourites: Boolean = false
    lateinit var imageData: Images
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = layoutInflater.inflate(R.layout.fragment_photo, null)
        initViews(view)
        mainActivityViewModel = ViewModelProvider(
            this,
            MainActivityViewModelFactory(requireActivity().application)
        )[MainActivityViewModel::class.java]

        strDate = arguments?.getString(IMAGE_DATE)
        if (arguments?.containsKey(IMAGE_DATA) == true) {
            isFavourites = true
            imageData = arguments?.getSerializable(IMAGE_DATA) as Images
        }

        if (::imageData.isInitialized && imageData != null) {
            initData(imageData)
        } else if (!TextUtils.isEmpty(strDate)) {
            mainActivityViewModel!!.getAPOD(strDate!!).observe(viewLifecycleOwner, Observer {
                imageData = it
                initData(it)
            })
        } else {
            mainActivityViewModel!!.getImageOfTheDay(Utils.getCurrentDate())
                .observe(viewLifecycleOwner, Observer {
                    imageData = it
                    initData(it)
                })
        }

        return view
    }


    private fun initViews(view: View) {
        imgImage = view.findViewById(R.id.imgImage)
        txtTitle = view.findViewById(R.id.txtTitle)
        txtExplanation = view.findViewById(R.id.txtExplationation)
        imgFavourite = view.findViewById(R.id.imgFavourite)
        imgFavourite!!.bringToFront()
        imgFavourite!!.setOnClickListener({
            mainActivityViewModel?.updateFavourites(imageData.date, !view.isSelected)
            imgFavourite!!.isSelected = !imgFavourite!!.isSelected
            if (isFavourites) {
                mainActivityViewModel!!.getAllFavourites()
            }
        })
    }

    private fun initData(image: Images) {
        if (image.mediaType.equals("video")) {
            Utils.loadImage(imgImage!!, image.thumUrl)
        } else {
            Utils.loadImage(imgImage!!, image.hdurl)
        }
        txtTitle!!.setText(image.title)
        txtExplanation!!.setText(image.explanation)
        imgFavourite!!.isSelected = if (image.isFav == 1) true else false
    }
}