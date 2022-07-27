package com.gs.apod.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gs.apod.repository.local.LocalRepository
import com.gs.apod.repository.local.tables.Images
//import com.gs.apod.repository.model.Image
import com.gs.apod.repository.remote.APIService
import com.gs.apod.repository.remote.ApiResponse
import com.gs.apod.repository.remote.RemoteRepository
import com.gs.apod.repository.remote.RetrofitUtil
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class Repository(
    private val context: Context,
    private val compositeDisposable: CompositeDisposable
) {
    private val imageResponse = MutableLiveData<Images>()
    private val localRepository = LocalRepository(context)
    private val apiService: APIService = RetrofitUtil.apiService
    private val remoteRepository = RemoteRepository(apiService, context)

    fun getImage(isInternetAvailable: Boolean, date: String): LiveData<ApiResponse> {
        var imageData = MutableLiveData<ApiResponse>();
        val observable =
            localRepository.getTodaysAPODFromDatabase(date)?.toObservable()
                ?.subscribeOn(Schedulers.io())
                ?.subscribe({
                    if (it == null) {
                        getTodaysAPOD(date, imageData)
                    } else {
                        val response = ApiResponse(it, null, false);
                        imageData.postValue(response)
                    }
                }, {
                    it.printStackTrace()
//                    val response = ApiResponse(null, it.message, false);
//                    imageData.postValue(response)
                    getTodaysAPOD(date, imageData)
                })
        compositeDisposable.add(observable)

        return imageData
    }

    fun getAllFavourites(): LiveData<List<Images>> {
        var imageData = MutableLiveData<List<Images>>()
        val observable =
            localRepository.getFavImagesFromDatabase()?.toObservable()?.subscribeOn(Schedulers.io())
                ?.subscribe({
                    imageData.postValue(it)
                }, {
                    it.printStackTrace()
                })
        compositeDisposable.add(observable)
        return imageData
    }

    fun getTodaysAPOD(date: String, imageData: MutableLiveData<ApiResponse>): LiveData<ApiResponse> {
        val observable =
            remoteRepository.getAPODFromRemote(date)?.toObservable()
                ?.subscribeOn(Schedulers.io())
                ?.subscribe({
                    if (it.isSuccessful) {
                        it.body()?.let { images ->
                            localRepository.saveImageData(images)?.toObservable()?.subscribe {
                                val response = ApiResponse(images, "", true);
                                imageData.postValue(response)
                            }
                        }
                    } else {
                        val response = ApiResponse(null, it.message(), false);
                        imageData.postValue(response)
                    }

                }, {
                    it.printStackTrace()
                    val response = ApiResponse(null, it.message, false);
                    imageData.postValue(response)
                })
        compositeDisposable.add(observable)
        return imageData
    }

    fun updateFavourite(date: String, isFavourite: Boolean) {
        val observable = localRepository.updateFav(date, isFavourite)?.toObservable()
            ?.subscribeOn(Schedulers.io())?.subscribe({
            }, {
                it.printStackTrace()
            })
        compositeDisposable.add(observable)
    }
}