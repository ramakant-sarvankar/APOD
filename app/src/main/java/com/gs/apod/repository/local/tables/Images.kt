package com.gs.apod.repository.local.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "images")
data class Images(
    @PrimaryKey
    @SerializedName("date")
    var date: String,
    @SerializedName("explanation")
    var explanation: String,
    @SerializedName("hdurl")
    var hdurl: String?,
    @SerializedName("media_type")
    var mediaType: String,
    @SerializedName("title")
    var title: String,
    @SerializedName("url")
    var url: String?,
    @ColumnInfo(name = "thumbnail_url", defaultValue = "")
    @SerializedName("thumbnail_url")
    var thumUrl: String?,
    @ColumnInfo(defaultValue = "0")
    var isFav: Int
) : Serializable
