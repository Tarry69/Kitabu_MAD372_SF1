package com.example.kitabu_mad372_sf1.data

import androidx.room.Embedded
import androidx.room.Relation

data class BookingWithBook(
    @Embedded val booking: BookingEntity,
    @Relation(
        parentColumn = "bookOwnerId",
        entityColumn = "bookId"
    )
    val book: BookEntity
)
