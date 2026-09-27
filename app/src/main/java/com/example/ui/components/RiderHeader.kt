package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.ui.theme.RiderPrimary
import com.example.ui.theme.RiderSecondary
import com.example.util.AppLanguage
import com.example.util.StringsHelper

@Composable
fun RiderHeader(
    language: AppLanguage,
    isTableView: Boolean,
    onLanguageClick: () -> Unit,
    onToggleView: () -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(RiderPrimary)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Brand & Subtitle
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.15f))
                        .padding(6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.TwoWheeler,
                        contentDescription = "Rider Logo",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = StringsHelper.get("brand_name", language),
                        color = Color.White,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.2.sp
                    )
                    Text(
                        text = StringsHelper.get("brand_subtitle", language),
                        color = Color.White.copy(alpha = 0.82f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Right Actions
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Table / Card View Toggle Button
                IconButton(
                    onClick = onToggleView,
                    modifier = Modifier
                        .testTag("btn_toggle_view")
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.16f))
                ) {
                    Icon(
                        imageVector = if (isTableView) Icons.Filled.List else Icons.Filled.TableChart,
                        contentDescription = if (isTableView) "Switch to Cards" else "Switch to Table",
                        tint = Color.White
                    )
                }

                // Add Order Button
                IconButton(
                    onClick = onAddClick,
                    modifier = Modifier
                        .testTag("btn_add_order")
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.16f))
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Delivery Order",
                        tint = Color.White
                    )
                }

                // Language Pill (.lang button from web mockup)
                Box(
                    modifier = Modifier
                        .testTag("btn_language")
                        .clip(CircleShape)
                        .background(RiderSecondary)
                        .clickable(onClick = onLanguageClick)
                        .padding(horizontal = 14.dp, vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = language.code,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}
