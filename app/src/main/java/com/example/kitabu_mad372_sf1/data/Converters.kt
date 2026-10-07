package com.example.kitabu_mad372_sf1.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromBookingStatus(status: BookingStatus): String {
        return status.name
    }

    @TypeConverter
    fun toBookingStatus(statusStr: String): BookingStatus {
        return BookingStatus.valueOf(statusStr)
    }
}
