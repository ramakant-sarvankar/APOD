package com.gs.apod.repository.local.dao

import androidx.room.*
import com.gs.apod.repository.local.tables.Images
import io.reactivex.rxjava3.core.Single

@Dao
interface ImagesDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertImage(image: Images): Single<Long>

    @Query("Select * from images where isFav = 1")
    fun gelAllFavouriteImages(): Single<List<Images>>

    @Query("UPDATE images SET isFav = :isFav  WHERE date = :date")
    fun updateImage(date: String, isFav: Int): Single<Int>

    @Query("Select * from images where date = :strDate")
    fun getAPOD(strDate: String): Single<Images>
}