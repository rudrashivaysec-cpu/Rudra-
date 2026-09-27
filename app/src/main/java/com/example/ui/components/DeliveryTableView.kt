package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DeliveryOrder
import com.example.ui.theme.RiderPrimary
import com.example.util.AppLanguage
import com.example.util.StringsHelper

@Composable
fun DeliveryTableView(
    orders: List<DeliveryOrder>,
    language: AppLanguage,
    onOrderClick: (DeliveryOrder) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("delivery_table_card")
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            // Header: "Delivery Details"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = StringsHelper.get("delivery_details", language),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = RiderPrimary
                )
                Text(
                    text = "Swipe horizontally ⇄",
                    fontSize = 10.5.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (orders.isEmpty()) {
                // Empty state (.empty from mockup)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Inbox,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            text = StringsHelper.get("no_deliveries", language),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            } else {
                // Horizontally scrollable table container (.tw from mockup)
                val horizontalScrollState = rememberScrollState()

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .horizontalScroll(horizontalScrollState)
                ) {
                    Column(
                        modifier = Modifier.width(720.dp)
                    ) {
                        // Table Header row (th)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(RiderPrimary)
                                .padding(horizontal = 8.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = StringsHelper.get("order_id", language),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.5.sp,
                                modifier = Modifier.width(90.dp)
                            )
                            Text(
                                text = StringsHelper.get("customer", language),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.5.sp,
                                modifier = Modifier.width(130.dp)
                            )
                            Text(
                                text = StringsHelper.get("address", language),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.5.sp,
                                modifier = Modifier.width(200.dp)
                            )
                            Text(
                                text = StringsHelper.get("location_map", language),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.5.sp,
                                modifier = Modifier.width(180.dp)
                            )
                            Text(
                                text = StringsHelper.get("status", language),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.5.sp,
                                modifier = Modifier.width(110.dp)
                            )
                        }

                        // Table Body rows (tr)
                        orders.forEachIndexed { index, order ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onOrderClick(order) }
                                    .background(
                                        if (index % 2 == 1) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                                        else MaterialTheme.colorScheme.surface
                                    )
                                    .padding(horizontal = 8.dp, vertical = 9.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Order ID (.oid)
                                Text(
                                    text = order.orderId,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.5.sp,
                                    color = RiderPrimary,
                                    modifier = Modifier.width(90.dp)
                                )

                                // Customer
                                Column(modifier = Modifier.width(130.dp)) {
                                    Text(
                                        text = order.customerName,
                                        fontSize = 12.5.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1
                                    )
                                    if (order.customerPhone.isNotBlank()) {
                                        Text(
                                            text = order.customerPhone,
                                            fontSize = 10.5.sp,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                            maxLines = 1
                                        )
                                    }
                                }

                                // Address (.addr)
                                Text(
                                    text = order.address,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 16.sp,
                                    modifier = Modifier.width(200.dp)
                                )

                                // Actions (Location Map)
                                Box(modifier = Modifier.width(180.dp)) {
                                    MapActionButtons(
                                        order = order,
                                        language = language,
                                        compact = true
                                    )
                                }

                                // Status Badge
                                Box(
                                    modifier = Modifier.width(110.dp),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    StatusBadge(
                                        status = order.status,
                                        onClick = { onOrderClick(order) }
                                    )
                                }
                            }

                            if (index < orders.size - 1) {
                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                    thickness = 1.dp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
