package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [DeliveryOrder::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun deliveryDao(): DeliveryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "rider_track_database"
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.deliveryDao())
                    }
                }
            }
        }

        suspend fun populateInitialData(dao: DeliveryDao) {
            val initialOrders = listOf(
                DeliveryOrder(
                    orderId = "#10244",
                    customerName = "Rahul Sharma",
                    customerPhone = "+91 98765 43210",
                    address = "Sector 62, Noida",
                    latitude = 28.6273,
                    longitude = 77.3719,
                    status = "Pending",
                    packageType = "Electronics & Gadgets",
                    codAmount = 0.0,
                    isPrepaid = true,
                    instructions = "Call before reaching gate 4. Leave with reception if unavailable."
                ),
                DeliveryOrder(
                    orderId = "#10245",
                    customerName = "Priya Verma",
                    customerPhone = "+91 98112 34567",
                    address = "Indiranagar, Bengaluru",
                    latitude = 12.9719,
                    longitude = 77.6412,
                    status = "Out for delivery",
                    packageType = "Apparel & Shoes",
                    codAmount = 450.0,
                    isPrepaid = false,
                    instructions = "Ring doorbell twice, Apt 302, 3rd floor."
                ),
                DeliveryOrder(
                    orderId = "#10246",
                    customerName = "Amit Singh",
                    customerPhone = "+91 97234 56789",
                    address = "Andheri West, Mumbai",
                    latitude = null,
                    longitude = null,
                    status = "Delivered",
                    packageType = "Home Essentials",
                    codAmount = 0.0,
                    isPrepaid = true,
                    instructions = "Delivered to security desk per customer request."
                ),
                DeliveryOrder(
                    orderId = "#10247",
                    customerName = "Sneha Kulkarni",
                    customerPhone = "+91 99001 88223",
                    address = "Cyber City, DLF Phase 2, Gurugram",
                    latitude = 28.4906,
                    longitude = 77.0899,
                    status = "Pending",
                    packageType = "Gourmet Food Parcel",
                    codAmount = 850.0,
                    isPrepaid = false,
                    instructions = "Handle with care - hot food container."
                ),
                DeliveryOrder(
                    orderId = "#10248",
                    customerName = "Vikram Reddy",
                    customerPhone = "+91 98450 12399",
                    address = "Banjara Hills, Road No. 12, Hyderabad",
                    latitude = 17.4156,
                    longitude = 78.4350,
                    status = "Out for delivery",
                    packageType = "Medicines & Healthcare",
                    codAmount = 0.0,
                    isPrepaid = true,
                    instructions = "Urgent prescription order - deliver directly to customer."
                )
            )
            dao.insertAll(initialOrders)
        }
    }
}
