package com.kozik.justyna.qrsound.services.data.response

import com.google.gson.annotations.SerializedName

data class SoundResponse(
    @SerializedName("hash")
    val hash: String,
    @SerializedName("description")
    val description: String = ""
)