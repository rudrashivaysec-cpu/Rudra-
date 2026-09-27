package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DeliveryOrder
import com.example.ui.theme.RiderPrimary
import com.example.ui.theme.RiderSecondary
import com.example.util.AppLanguage
import com.example.util.MapUtils
import com.example.util.StringsHelper

@Composable
fun MapActionButtons(
    order: DeliveryOrder,
    language: AppLanguage,
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    val context = LocalContext.current
    val hasPin = order.latitude != null && order.longitude != null

    val mapLabel = if (hasPin) {
        StringsHelper.get("view_map", language)
    } else {
        StringsHelper.get("find_address", language)
    }
    val navLabel = StringsHelper.get("navigate", language)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Map Link Button
        OutlinedButton(
            onClick = { MapUtils.openMap(context, order) },
            modifier = Modifier
                .defaultMinSize(minWidth = 48.dp, minHeight = 36.dp)
                .testTag("btn_map_${order.id}"),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.5.dp, RiderSecondary),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = RiderSecondary,
                containerColor = Color.Transparent
            ),
            contentPadding = PaddingValues(horizontal = if (compact) 8.dp else 10.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = if (hasPin) Icons.Filled.LocationOn else Icons.Filled.Search,
                contentDescription = null,
                tint = RiderSecondary,
                modifier = Modifier.padding(end = 4.dp)
            )
            Text(
                text = mapLabel,
                fontSize = if (compact) 11.5.sp else 12.sp,
                fontWeight = FontWeight.Bold,
                color = RiderSecondary
            )
        }

        // Navigate Directions Button
        OutlinedButton(
            onClick = { MapUtils.openNavigation(context, order) },
            modifier = Modifier
                .defaultMinSize(minWidth = 48.dp, minHeight = 36.dp)
                .testTag("btn_nav_${order.id}"),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.5.dp, RiderPrimary),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = RiderPrimary,
                containerColor = Color.Transparent
            ),
            contentPadding = PaddingValues(horizontal = if (compact) 8.dp else 10.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Navigation,
                contentDescription = null,
                tint = RiderPrimary,
                modifier = Modifier.padding(end = 4.dp)
            )
            Text(
                text = navLabel,
                fontSize = if (compact) 11.5.sp else 12.sp,
                fontWeight = FontWeight.Bold,
                color = RiderPrimary
            )
        }
    }
}
