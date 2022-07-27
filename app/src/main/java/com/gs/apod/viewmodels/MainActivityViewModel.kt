package com.gs.apod.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gs.apod.repository.InternetConnections
import com.gs.apod.repository.Repository
import com.gs.apod.repository.local.tables.Images
import com.gs.apod.repository.remote.ApiResponse
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.launch

//import com.gs.apod.repository.model.Image

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    val compositeDisposable = CompositeDisposable()
    private val mImage = MutableLiveData<Images>()
    private val mImageDay = MutableLiveData<Images>()
    private var imageFavData = MutableLiveData<List<Images>>()
    private var internetConnections = InternetConnections(application)
    private var isInternetAvailable : Boolean = false
    init {
        internetConnections.observeForever({ isConnected ->
            isInternetAvailable = isConnected
        })
    }

    val repository = Repository(application, compositeDisposable)

    fun getImageOfTheDay(date : String) : LiveData<Images>{
        viewModelScope.launch {
            repository.getImage(isInternetAvailable,date).observeForever() {
                mImage.setValue(it.images)
            }
        }
        return mImage
    }

    fun getAPOD(date : String) : LiveData<Images>{
        viewModelScope.launch {
            repository.getImage(isInternetAvailable,date).observeForever() {
                mImageDay.setValue(it.images)
            }
        }
        return mImageDay
    }

    fun getAllFavourites() : LiveData<List<Images>>{
        viewModelScope.launch {
            repository.getAllFavourites().observeForever() {
                imageFavData.setValue(it)
            }
        }
        return imageFavData
    }

    fun updateFavourites(date : String, isFavourite : Boolean){
        viewModelScope.launch {
            repository.updateFavourite(date, isFavourite)
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}