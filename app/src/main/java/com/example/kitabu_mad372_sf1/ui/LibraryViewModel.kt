package com.example.kitabu_mad372_sf1.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.kitabu_mad372_sf1.data.BookEntity
import com.example.kitabu_mad372_sf1.data.BookingStatus
import com.example.kitabu_mad372_sf1.data.BookWithBooking
import com.example.kitabu_mad372_sf1.repository.LibraryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LibraryViewModel(private val repository: LibraryRepository) : ViewModel() {

    val searchQuery = MutableStateFlow("")
    val selectedDuration = MutableStateFlow(7)
    val showDialog = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val books: StateFlow<List<BookEntity>> = searchQuery
        .flatMapLatest { query ->
            repository.getBooks(query)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val activeReservations: StateFlow<List<BookWithBooking>> = repository.getActiveReservations()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onSearchQueryChange(query: String) {
        searchQuery.value = query
    }

    fun reserveBook(bookId: Long, userName: String, durationDays: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val bookingId = repository.reserveBook(bookId, userName, durationDays)
            delay(3000)
            repository.updateBookingStatus(bookingId, BookingStatus.READY_FOR_PICKUP)
        }
    }

    fun cancelReservation(bookingId: Long, bookId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.cancelBooking(bookingId, bookId)
        }
    }

    fun renewBook(bookingId: Long, extraDays: Int = 7) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.extendBookingDeadline(bookingId, extraDays)
        }
    }

    fun returnBook(bookingId: Long, bookId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.returnBookTransaction(bookingId, bookId)
        }
    }

    // Aliases for backward compatibility
    fun cancelBooking(bookingId: Long, bookId: Long) = cancelReservation(bookingId, bookId)
    fun renewBooking(bookingId: Long, extraDays: Int) = renewBook(bookingId, extraDays)

    class Factory(private val repository: LibraryRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LibraryViewModel::class.java)) {
                return LibraryViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
