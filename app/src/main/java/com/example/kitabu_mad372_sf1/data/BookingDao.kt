package com.example.kitabu_mad372_sf1.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {
    @Insert
    suspend fun insertBooking(booking: BookingEntity): Long

    @Transaction
    @Query("SELECT * FROM bookings WHERE status != 'RETURNED'")
    fun getActiveBookingsWithDetails(): Flow<List<BookWithBooking>>

    @Query("UPDATE bookings SET returnDeadline = :newDeadline WHERE bookingId = :bookingId")
    suspend fun extendDeadline(bookingId: Long, newDeadline: Long): Int

    @Query("UPDATE bookings SET status = :status WHERE bookingId = :bookingId")
    suspend fun updateStatus(bookingId: Long, status: BookingStatus): Int

    @Query("DELETE FROM bookings WHERE bookingId = :bookingId")
    suspend fun deleteBooking(bookingId: Long): Int

    @Query("SELECT * FROM bookings WHERE bookingId = :bookingId")
    suspend fun getBookingById(bookingId: Long): BookingEntity?
}
