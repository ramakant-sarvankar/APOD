package com.gs.apod.repository.local

import android.content.Context
import com.gs.apod.repository.local.tables.Images
//import com.gs.apod.repository.model.Image
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class LocalRepository(
    private val context: Context
) {
    private val database = APODDatabase.getInstance(context)

    fun getTodaysAPODFromDatabase(date: String): Single<Images>? =
        database?.imagesDao()?.getAPOD(date)
            ?.subscribeOn(Schedulers.io())

    fun getFavImagesFromDatabase(): Single<List<Images>>? =
        database?.imagesDao()?.gelAllFavouriteImages()
            ?.subscribeOn(Schedulers.io())


    fun updateFav(date: String, isFav: Boolean) =
        database?.imagesDao()?.updateImage(date, if (isFav) 1 else 0)
            ?.subscribeOn(Schedulers.io())

    fun saveImageData(model: Images) = database?.imagesDao()?.insertImage(model)
        ?.subscribeOn(Schedulers.io())

}