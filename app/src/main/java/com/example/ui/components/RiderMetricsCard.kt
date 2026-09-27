package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.RiderPrimary
import com.example.ui.theme.RiderSecondary
import com.example.util.AppLanguage
import com.example.util.StringsHelper
import com.example.viewmodel.RiderUiState

@Composable
fun RiderMetricsCard(
    uiState: RiderUiState,
    onFilterSelect: (String?) -> Unit,
    onSearchChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Hero Visual Banner
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp)
            ) {
                // Banner illustration
                Image(
                    painter = painterResource(id = R.drawable.rider_hero_banner_1790347066616),
                    contentDescription = "Delivery Hero Banner",
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )

                // Dark gradient overlay for text legibility
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xEE0D47A1),
                                    Color(0x990D47A1),
                                    Color.Transparent
                                )
                            )
                        )
                )

                Row(
                    modifier = Modifier
                        .matchParentSize()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Shift Manifest",
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "${uiState.pendingCount + uiState.outForDeliveryCount} active tasks remaining today",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            MetricPill(
                                label = "Total",
                                value = "${uiState.totalCount}",
                                color = RiderSecondary
                            )
                            if (uiState.totalCodAmount > 0) {
                                MetricPill(
                                    label = "COD Due",
                                    value = "₹${uiState.totalCodAmount.toInt()}",
                                    color = Color(0xFFFFC93C)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Metrics Quick Glance Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            QuickStatCard(
                modifier = Modifier.weight(1f),
                title = StringsHelper.get("pending", uiState.language),
                count = "${uiState.pendingCount}",
                icon = Icons.Filled.PendingActions,
                color = Color(0xFFF5A400),
                selected = uiState.statusFilter == "Pending",
                onClick = {
                    onFilterSelect(if (uiState.statusFilter == "Pending") null else "Pending")
                }
            )

            QuickStatCard(
                modifier = Modifier.weight(1f),
                title = StringsHelper.get("out_for_delivery", uiState.language),
                count = "${uiState.outForDeliveryCount}",
                icon = Icons.Filled.DeliveryDining,
                color = RiderPrimary,
                selected = uiState.statusFilter == "Out for delivery",
                onClick = {
                    onFilterSelect(if (uiState.statusFilter == "Out for delivery") null else "Out for delivery")
                }
            )

            QuickStatCard(
                modifier = Modifier.weight(1f),
                title = StringsHelper.get("delivered", uiState.language),
                count = "${uiState.deliveredCount}",
                icon = Icons.Filled.CheckCircle,
                color = Color(0xFF2E9E4F),
                selected = uiState.statusFilter == "Delivered",
                onClick = {
                    onFilterSelect(if (uiState.statusFilter == "Delivered") null else "Delivered")
                }
            )
        }

        // Search Bar
        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = onSearchChange,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_search_orders"),
            placeholder = {
                Text(
                    text = "Search by Order #, Customer, or Area...",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search",
                    tint = RiderPrimary
                )
            },
            trailingIcon = {
                if (uiState.searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchChange("") }) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "Clear search",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedBorderColor = RiderPrimary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)
            )
        )

        // Filter Pills Row
        val filters = listOf(
            null to StringsHelper.get("all", uiState.language),
            "Pending" to StringsHelper.get("pending", uiState.language),
            "Out for delivery" to StringsHelper.get("out_for_delivery", uiState.language),
            "Delivered" to StringsHelper.get("delivered", uiState.language),
            "Failed" to StringsHelper.get("failed", uiState.language)
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filters) { (filterValue, label) ->
                val isSelected = uiState.statusFilter == filterValue
                FilterChip(
                    selected = isSelected,
                    onClick = { onFilterSelect(filterValue) },
                    label = {
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = RiderPrimary,
                        selectedLabelColor = Color.White,
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = if (isSelected) RiderPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                        enabled = true,
                        selected = isSelected
                    )
                )
            }
        }
    }
}

@Composable
private fun MetricPill(
    label: String,
    value: String,
    color: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.25f))
            .border(1.dp, color, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "$label:",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun QuickStatCard(
    title: String,
    count: String,
    icon: ImageVector,
    color: Color,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) color.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.5.dp,
            if (selected) color else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
        ),
        modifier = modifier
            .clickable(onClick = onClick)
            .testTag("quick_stat_${title.lowercase()}")
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = count,
                    color = color,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                maxLines = 1
            )
        }
    }
}
