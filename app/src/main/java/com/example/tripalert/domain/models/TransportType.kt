package com.example.tripalert.domain.models

import com.google.gson.annotations.SerializedName

enum class TransportType {
    @SerializedName("walk") WALK,
    @SerializedName("car") CAR,
    @SerializedName("bus") BUS,
    @SerializedName("taxi") TAXI,
    @SerializedName("scooter") SCOOTER
}