package com.gs.apod.repository.remote

import com.gs.apod.repository.local.tables.Images
import io.reactivex.internal.operators.maybe.MaybeDoAfterSuccess

data class ApiResponse(
    val images: Images? = null,
    val errorMessage : String? = null,
    val isSuccess: Boolean
)
