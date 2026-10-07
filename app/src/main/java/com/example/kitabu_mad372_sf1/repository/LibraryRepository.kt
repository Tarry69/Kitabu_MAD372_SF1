package com.example.kitabu_mad372_sf1.repository

import com.example.kitabu_mad372_sf1.data.BookDao
import com.example.kitabu_mad372_sf1.data.BookEntity
import com.example.kitabu_mad372_sf1.data.BookingDao
import com.example.kitabu_mad372_sf1.data.BookingEntity
import com.example.kitabu_mad372_sf1.data.BookingStatus
import com.example.kitabu_mad372_sf1.data.BookWithBooking
import kotlinx.coroutines.flow.Flow

class LibraryRepository(
    private val bookDao: BookDao,
    private val bookingDao: BookingDao
) {
    fun getBooks(searchQuery: String): Flow<List<BookEntity>> {
        return if (searchQuery.isBlank()) {
            bookDao.getAllBooks()
        } else {
            bookDao.searchBooks(searchQuery)
        }
    }

    fun getActiveReservations(): Flow<List<BookWithBooking>> {
        return bookingDao.getActiveBookingsWithDetails()
    }

    suspend fun reserveBook(bookId: Long, userName: String, durationDays: Int): Long {
        bookDao.updateAvailability(bookId, false)
        val now = System.currentTimeMillis()
        val deadline = now + (durationDays * 24L * 60L * 60L * 1000L)
        val booking = BookingEntity(
            bookOwnerId = bookId,
            userName = userName,
            bookingDate = now,
            returnDeadline = deadline,
            status = BookingStatus.PENDING
        )
        return bookingDao.insertBooking(booking)
    }

    suspend fun updateBookingStatus(bookingId: Long, status: BookingStatus) {
        bookingDao.updateStatus(bookingId, status)
    }

    suspend fun cancelBooking(bookingId: Long, bookId: Long) {
        bookingDao.deleteBooking(bookingId)
        bookDao.updateAvailability(bookId, true)
    }

    suspend fun extendBookingDeadline(bookingId: Long, extraDays: Int) {
        val booking = bookingDao.getBookingById(bookingId)
        if (booking != null) {
            val newDeadline = booking.returnDeadline + (extraDays * 24L * 60L * 60L * 1000L)
            bookingDao.extendDeadline(bookingId, newDeadline)
        }
    }

    suspend fun returnBookTransaction(bookingId: Long, bookId: Long) {
        bookingDao.updateStatus(bookingId, BookingStatus.RETURNED)
        bookDao.updateAvailability(bookId, true)
    }
}
