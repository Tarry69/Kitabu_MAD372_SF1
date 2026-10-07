package com.example.kitabu_mad372_sf1.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.kitabu_mad372_sf1.data.BookingStatus
import com.example.kitabu_mad372_sf1.data.BookWithBooking
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDashboardScreen(
    viewModel: LibraryViewModel,
    onNavigateToCatalog: () -> Unit
) {
    val activeReservations by viewModel.activeReservations.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User Library Dashboard") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Your Active Rentals & Reservations",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (activeReservations.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No active rentals or reservations found.", fontSize = 16.sp, color = Color.Gray)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(activeReservations) { bookingWithBook ->
                        ActiveRentalCard(
                            bookingWithBook = bookingWithBook,
                            onRenew = { viewModel.renewBook(bookingWithBook.booking.bookingId, 7) },
                            onReturn = { viewModel.returnBook(bookingWithBook.booking.bookingId, bookingWithBook.booking.bookOwnerId) },
                            onCancel = { viewModel.cancelReservation(bookingWithBook.booking.bookingId, bookingWithBook.booking.bookOwnerId) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ActiveRentalCard(
    bookingWithBook: BookWithBooking,
    onRenew: () -> Unit,
    onReturn: () -> Unit,
    onCancel: () -> Unit
) {
    val booking = bookingWithBook.booking
    val book = bookingWithBook.book

    val daysRemaining = remember(booking.returnDeadline) {
        val msLeft = booking.returnDeadline - System.currentTimeMillis()
        val days = msLeft / (24 * 60 * 60 * 1000L)
        max(0L, days)
    }

    val sdf = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val formattedDeadline = remember(booking.returnDeadline) {
        sdf.format(Date(booking.returnDeadline))
    }
    val formattedDate = remember(booking.bookingDate) {
        sdf.format(Date(booking.bookingDate))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                AsyncImage(
                    model = book.imageUrl,
                    contentDescription = "Cover for ${book.title}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(70.dp)
                        .height(100.dp)
                        .clip(RoundedCornerShape(6.dp))
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = book.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "By ${book.author}",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Status Badges
                    when (booking.status) {
                        BookingStatus.PENDING -> {
                            Surface(
                                color = Color(0xFFFFC107), // Amber #FFC107
                                contentColor = Color.Black,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Pending Approval",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        BookingStatus.READY_FOR_PICKUP -> {
                            Surface(
                                color = Color(0xFF29B6F6), // Blue #29B6F6
                                contentColor = Color.White,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Ready for Pickup",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        BookingStatus.BORROWED -> {
                            Surface(
                                color = Color(0xFF4CAF50), // Green #4CAF50
                                contentColor = Color.White,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Borrowed",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        BookingStatus.RETURNED -> {
                            Surface(
                                color = Color.Gray,
                                contentColor = Color.White,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Returned",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = "Reserved by: ${booking.userName}", fontSize = 13.sp)
            Text(text = "Reservation Date: $formattedDate", fontSize = 13.sp)

            // Dynamic Action Buttons & Information per BookingStatus
            when (booking.status) {
                BookingStatus.PENDING -> {
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onCancel,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cancel Request")
                    }
                }
                BookingStatus.READY_FOR_PICKUP, BookingStatus.BORROWED -> {
                    Text(text = "Return Deadline: $formattedDeadline", fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Days Remaining: $daysRemaining day(s)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (daysRemaining <= 2) Color.Red else Color(0xFF2E7D32)
                    )
                    if (booking.status == BookingStatus.READY_FOR_PICKUP) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Instructions: Please collect your reserved book at the main library desk within 48 hours.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onRenew,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Renew (+7 Days)")
                        }
                        Button(
                            onClick = onReturn,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Return Book")
                        }
                    }
                }
                BookingStatus.RETURNED -> {
                    // No action buttons required
                }
            }
        }
    }
}
