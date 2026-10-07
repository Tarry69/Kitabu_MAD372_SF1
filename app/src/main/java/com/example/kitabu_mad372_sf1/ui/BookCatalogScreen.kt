package com.example.kitabu_mad372_sf1.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.kitabu_mad372_sf1.data.BookEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookCatalogScreen(
    viewModel: LibraryViewModel,
    onNavigateToDashboard: () -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val books by viewModel.books.collectAsStateWithLifecycle()

    var selectedCategory by remember { mutableStateOf("All books") }
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedBook by remember { mutableStateOf<BookEntity?>(null) }
    var userNameInput by remember { mutableStateOf("") }
    var durationInput by remember { mutableStateOf(7) }

    val categories = listOf("All books", "Science", "Design", "Humanities", "Computer Science", "Software Engineering")

    val filteredBooks = remember(books, selectedCategory) {
        if (selectedCategory == "All books") {
            books
        } else {
            books.filter { it.category.equals(selectedCategory, ignoreCase = true) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .background(Color(0xFF1B2269), shape = RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Book,
                                    contentDescription = "Kitabu Logo",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Kitabu Library",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1B2269)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(38.dp)
                                .background(Color(0xFFECEFFE), shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "AK",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1B2269)
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Search Input Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                placeholder = { Text("Search books, authors, or ISBN", color = Color.Gray, fontSize = 14.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color(0xFF1B2269)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { /* Filter action */ }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filter",
                            tint = Color(0xFF1B2269)
                        )
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF8F9FA),
                    unfocusedContainerColor = Color(0xFFF8F9FA),
                    focusedBorderColor = Color(0xFF1B2269),
                    unfocusedBorderColor = Color(0xFFE0E0E0)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Category Chips (Bubbles)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                items(categories) { category ->
                    val isSelected = category == selectedCategory
                    Surface(
                        onClick = { selectedCategory = category },
                        shape = RoundedCornerShape(50),
                        color = if (isSelected) Color(0xFFECEFFE) else Color.White,
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (isSelected) Color(0xFF1B2269) else Color(0xFFE0E0E0)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = Color(0xFF1B2269),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                            }
                            Text(
                                text = category,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color(0xFF1B2269) else Color(0xFF616161)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Book Catalog List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(filteredBooks) { book ->
                    BookCardItem(
                        book = book,
                        onReserveClick = {
                            selectedBook = book
                            userNameInput = ""
                            durationInput = 7
                            showBottomSheet = true
                        }
                    )
                }
            }
        }
    }

    if (showBottomSheet && selectedBook != null) {
        AlertDialog(
            onDismissRequest = { showBottomSheet = false },
            title = { Text("Confirm Reservation") },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Book: ${selectedBook?.title}", fontWeight = FontWeight.Bold)
                    Text(text = "Author: ${selectedBook?.author}", fontSize = 14.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = userNameInput,
                        onValueChange = { userNameInput = it },
                        label = { Text("User Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "Select Rental Duration:", fontWeight = FontWeight.SemiBold)
                    val options = listOf(7, 14, 21)
                    options.forEach { days ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = (durationInput == days),
                                    onClick = { durationInput = days },
                                    role = Role.RadioButton
                                )
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (durationInput == days),
                                onClick = null
                            )
                            Text(
                                text = "$days Days",
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (userNameInput.isNotBlank() && selectedBook != null) {
                            viewModel.reserveBook(selectedBook!!.bookId, userNameInput, durationInput)
                            showBottomSheet = false
                        }
                    },
                    enabled = userNameInput.isNotBlank()
                ) {
                    Text("Confirm Reservation")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBottomSheet = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun BookCardItem(
    book: BookEntity,
    onReserveClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = book.imageUrl,
                contentDescription = "Cover for ${book.title}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(80.dp)
                    .height(115.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = book.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Author: ${book.author}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))

                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = book.category,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Surface(
                    color = if (book.isAvailable) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                    contentColor = if (book.isAvailable) Color(0xFF2E7D32) else Color(0xFFC62828),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = if (book.isAvailable) "Available" else "Borrowed",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (book.isAvailable) {
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onReserveClick,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text("Reserve", fontSize = 13.sp)
                }
            }
        }
    }
}
