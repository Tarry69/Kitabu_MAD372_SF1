package com.example.kitabu_mad372_sf1.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "User Profile",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B2269)
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Profile Avatar
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(Color(0xFFECEFFE), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "AK",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B2269)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Alice Smith",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B2269)
            )

            Text(
                text = "Student ID: MAD372-2026",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Details Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ProfileDetailRow(
                        icon = Icons.Default.Email,
                        label = "Email",
                        value = "alice.smith@university.edu"
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFEEEEEE))
                    ProfileDetailRow(
                        icon = Icons.Default.LibraryBooks,
                        label = "Membership Status",
                        value = "Active Library Scholar"
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFEEEEEE))
                    ProfileDetailRow(
                        icon = Icons.Default.Settings,
                        label = "Preferences",
                        value = "Push Notifications On"
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileDetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(Color(0xFFECEFFE), shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color(0xFF1B2269),
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, fontSize = 12.sp, color = Color.Gray)
            Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1B2269))
        }
    }
}
