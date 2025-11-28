package com.example.tripalert.domain.models

import com.google.gson.annotations.SerializedName

enum class TransportType {
    @SerializedName("CAR") CAR,
    @SerializedName("BUS") BUS,
    @SerializedName("WALK") WALK,
    @SerializedName("BIKE") BIKE,
    @SerializedName("METRO") METRO
}