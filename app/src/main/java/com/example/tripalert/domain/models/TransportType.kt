package com.example.tripalert.domain.models

import com.google.gson.annotations.SerializedName

enum class TransportType {
    @SerializedName("walk") CAR,
    @SerializedName("car") BUS,
    @SerializedName("bus") WALK,
    @SerializedName("taxi") BIKE,
    @SerializedName("train") METRO
}