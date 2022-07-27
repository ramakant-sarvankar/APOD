package com.gs.apod.repository.remote;

import android.content.Context;
import com.gs.apod.repository.local.tables.Images
//import com.gs.apod.repository.model.Image
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Response

class RemoteRepository(
    private val apiService: APIService,
    private val context: Context
) {
    fun getTodaysAPODFromRemote(): Single<Response<Images>>? =
        apiService?.getTodaysAPOD()
            ?.subscribeOn(Schedulers.io())

    fun getAPODFromRemote(date: String): Single<Response<Images>>? =
        apiService?.getAPOD(date)
            ?.subscribeOn(Schedulers.io())

}
