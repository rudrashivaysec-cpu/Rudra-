package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DeliveryOrder
import com.example.ui.theme.RiderPrimary
import com.example.ui.theme.RiderSecondary
import com.example.ui.theme.StatusDeliveredBg
import com.example.ui.theme.StatusDeliveredText
import com.example.ui.theme.StatusFailedBg
import com.example.ui.theme.StatusFailedText
import com.example.ui.theme.StatusOutBg
import com.example.ui.theme.StatusOutText
import com.example.ui.theme.StatusPendingBg
import com.example.ui.theme.StatusPendingText
import com.example.util.AppLanguage
import com.example.util.MapUtils
import com.example.util.StringsHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryDetailSheet(
    order: DeliveryOrder,
    language: AppLanguage,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onStatusChange: (String) -> Unit,
    onDelete: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.testTag("delivery_detail_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Sheet Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = order.orderId,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = RiderPrimary
                    )
                    Text(
                        text = order.packageType,
                        fontSize = 12.5.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                StatusBadge(status = order.status)
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

            // Customer Info Box
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(RiderPrimary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = null,
                            tint = RiderPrimary
                        )
                    }

                    Column {
                        Text(
                            text = order.customerName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = if (order.customerPhone.isNotBlank()) order.customerPhone else "No phone provided",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }

                if (order.customerPhone.isNotBlank()) {
                    Button(
                        onClick = { MapUtils.callCustomer(context, order.customerPhone) },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RiderSecondary)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Call,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Call",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Delivery Destination & Coordinates
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = null,
                        tint = RiderPrimary,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                    Column {
                        Text(
                            text = StringsHelper.get("address", language),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = RiderPrimary
                        )
                        Text(
                            text = order.address,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (order.latitude != null && order.longitude != null) {
                            Text(
                                text = "GPS: ${order.latitude}, ${order.longitude}",
                                fontSize = 11.5.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }

            // Payment & Instructions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Payment Card
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(10.dp)
                ) {
                    Text(
                        text = "Payment Mode",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = if (order.isPrepaid) "Prepaid" else "Cash on Delivery",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (order.isPrepaid) Color(0xFF2E9E4F) else RiderSecondary
                    )
                    if (!order.isPrepaid && order.codAmount > 0) {
                        Text(
                            text = "Collect: ₹${order.codAmount.toInt()}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Assigned time Card
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(10.dp)
                ) {
                    Text(
                        text = "Last Updated",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    val dateStr = SimpleDateFormat("hh:mm a, dd MMM", Locale.getDefault()).format(Date(order.updatedTimestamp))
                    Text(
                        text = dateStr,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            if (order.instructions.isNotBlank()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(12.dp)
                ) {
                    Text(
                        text = StringsHelper.get("delivery_notes", language),
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = RiderPrimary
                    )
                    Text(
                        text = order.instructions,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Quick Map Navigation Action Buttons
            Text(
                text = "Map & Navigation",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = RiderPrimary
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { MapUtils.openMap(context, order) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RiderSecondary)
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "View Map", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { MapUtils.openNavigation(context, order) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RiderPrimary)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Navigation,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Navigate", fontWeight = FontWeight.Bold)
                }
            }

            // Status Update Section
            Text(
                text = StringsHelper.get("update_status", language),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = RiderPrimary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusButton(
                    title = "Pending",
                    isSelected = order.status.equals("Pending", ignoreCase = true),
                    bgColor = StatusPendingBg,
                    textColor = StatusPendingText,
                    onClick = { onStatusChange("Pending") },
                    modifier = Modifier.weight(1f)
                )

                StatusButton(
                    title = "Out for delivery",
                    isSelected = order.status.contains("out", ignoreCase = true),
                    bgColor = StatusOutBg,
                    textColor = StatusOutText,
                    onClick = { onStatusChange("Out for delivery") },
                    modifier = Modifier.weight(1.3f)
                )

                StatusButton(
                    title = "Delivered",
                    isSelected = order.status.contains("deliver", ignoreCase = true) && !order.status.contains("out", ignoreCase = true),
                    bgColor = StatusDeliveredBg,
                    textColor = StatusDeliveredText,
                    onClick = { onStatusChange("Delivered") },
                    modifier = Modifier.weight(1.1f)
                )

                StatusButton(
                    title = "Failed",
                    isSelected = order.status.contains("fail", ignoreCase = true),
                    bgColor = StatusFailedBg,
                    textColor = StatusFailedText,
                    onClick = { onStatusChange("Failed") },
                    modifier = Modifier.weight(1f)
                )
            }

            // Delete Order Action
            OutlinedButton(
                onClick = onDelete,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.5f)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
                Icon(
                    imageVector = Icons.Filled.DeleteOutline,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Delete Order from Manifest")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun StatusButton(
    title: String,
    isSelected: Boolean,
    bgColor: Color,
    textColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(if (isSelected) 2.dp else 1.dp, if (isSelected) textColor else Color.Transparent),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isSelected) bgColor else bgColor.copy(alpha = 0.5f),
            contentColor = textColor
        ),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 4.dp, vertical = 6.dp)
    ) {
        Text(
            text = title,
            fontSize = 10.5.sp,
            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
            maxLines = 1
        )
    }
}
