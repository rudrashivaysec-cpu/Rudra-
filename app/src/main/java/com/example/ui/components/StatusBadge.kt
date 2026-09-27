package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.StatusDeliveredBg
import com.example.ui.theme.StatusDeliveredBorder
import com.example.ui.theme.StatusDeliveredText
import com.example.ui.theme.StatusFailedBg
import com.example.ui.theme.StatusFailedBorder
import com.example.ui.theme.StatusFailedText
import com.example.ui.theme.StatusOutBg
import com.example.ui.theme.StatusOutBorder
import com.example.ui.theme.StatusOutText
import com.example.ui.theme.StatusPendingBg
import com.example.ui.theme.StatusPendingBorder
import com.example.ui.theme.StatusPendingText

enum class DeliveryStatus(val label: String) {
    PENDING("Pending"),
    OUT_FOR_DELIVERY("Out for delivery"),
    DELIVERED("Delivered"),
    FAILED("Failed")
}

fun normalizeStatus(status: String): DeliveryStatus {
    val s = status.lowercase()
    return when {
        s.contains("deliver") && !s.contains("out") -> DeliveryStatus.DELIVERED
        s.contains("out") -> DeliveryStatus.OUT_FOR_DELIVERY
        s.contains("fail") || s.contains("cancel") -> DeliveryStatus.FAILED
        else -> DeliveryStatus.PENDING
    }
}

@Composable
fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val normalized = normalizeStatus(status)
    val (bgColor, textColor, borderColor) = when (normalized) {
        DeliveryStatus.PENDING -> Triple(StatusPendingBg, StatusPendingText, StatusPendingBorder.copy(alpha = 0.4f))
        DeliveryStatus.OUT_FOR_DELIVERY -> Triple(StatusOutBg, StatusOutText, StatusOutBorder.copy(alpha = 0.5f))
        DeliveryStatus.DELIVERED -> Triple(StatusDeliveredBg, StatusDeliveredText, StatusDeliveredBorder.copy(alpha = 0.5f))
        DeliveryStatus.FAILED -> Triple(StatusFailedBg, StatusFailedText, StatusFailedBorder.copy(alpha = 0.5f))
    }

    Box(
        modifier = modifier
            .testTag("status_badge_${status.lowercase().replace(" ", "_")}")
            .background(bgColor, CircleShape)
            .border(1.dp, borderColor, CircleShape)
            .then(
                if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
            )
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = status,
            color = textColor,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
    }
}
