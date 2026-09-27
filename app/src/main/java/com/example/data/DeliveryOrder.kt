package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deliveries")
data class DeliveryOrder(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val orderId: String,          // e.g., "#10244"
    val customerName: String,     // e.g., "Rahul Sharma"
    val customerPhone: String = "",
    val address: String,          // e.g., "Sector 62, Noida"
    val latitude: Double? = null,
    val longitude: Double? = null,
    val status: String = "Pending", // "Pending", "Out for delivery", "Delivered", "Failed"
    val packageType: String = "Standard Parcel",
    val codAmount: Double = 0.0,
    val isPrepaid: Boolean = true,
    val instructions: String = "",
    val assignedTimestamp: Long = System.currentTimeMillis(),
    val updatedTimestamp: Long = System.currentTimeMillis()
)
