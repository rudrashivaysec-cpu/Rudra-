package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.data.DeliveryOrder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object MapUtils {

    fun getTarget(order: DeliveryOrder): String {
        return if (order.latitude != null && order.longitude != null) {
            "${order.latitude},${order.longitude}"
        } else {
            order.address.trim()
        }
    }

    fun openMap(context: Context, order: DeliveryOrder) {
        val target = getTarget(order)
        val encodedTarget = URLEncoder.encode(target, StandardCharsets.UTF_8.toString())
        val webUrl = "https://www.google.com/maps/search/?api=1&query=$encodedTarget"

        val geoUri = if (order.latitude != null && order.longitude != null) {
            Uri.parse("geo:${order.latitude},${order.longitude}?q=${order.latitude},${order.longitude}(${Uri.encode(order.customerName)})")
        } else {
            Uri.parse("geo:0,0?q=$encodedTarget")
        }

        try {
            val mapIntent = Intent(Intent.ACTION_VIEW, geoUri).apply {
                setPackage("com.google.android.apps.maps")
            }
            if (mapIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(mapIntent)
                return
            }
        } catch (_: Exception) {
            // Fallback
        }

        try {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(webUrl))
            context.startActivity(browserIntent)
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open map: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun openNavigation(context: Context, order: DeliveryOrder) {
        val target = getTarget(order)
        val encodedTarget = URLEncoder.encode(target, StandardCharsets.UTF_8.toString())
        val webNavUrl = "https://www.google.com/maps/dir/?api=1&destination=$encodedTarget&travelmode=driving"

        val navUri = Uri.parse("google.navigation:q=$encodedTarget&mode=d")

        try {
            val navIntent = Intent(Intent.ACTION_VIEW, navUri).apply {
                setPackage("com.google.android.apps.maps")
            }
            if (navIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(navIntent)
                return
            }
        } catch (_: Exception) {
            // Fallback
        }

        try {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(webNavUrl))
            context.startActivity(browserIntent)
        } catch (e: Exception) {
            Toast.makeText(context, "Could not start navigation: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun callCustomer(context: Context, phone: String) {
        if (phone.isBlank()) {
            Toast.makeText(context, "No phone number available", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
            context.startActivity(dialIntent)
        } catch (e: Exception) {
            Toast.makeText(context, "Unable to dial: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
