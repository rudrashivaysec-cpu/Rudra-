package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DeliveryDao {
    @Query("SELECT * FROM deliveries ORDER BY id ASC")
    fun getAllDeliveries(): Flow<List<DeliveryOrder>>

    @Query("SELECT * FROM deliveries WHERE status = :status ORDER BY id ASC")
    fun getDeliveriesByStatus(status: String): Flow<List<DeliveryOrder>>

    @Query("SELECT * FROM deliveries WHERE id = :id LIMIT 1")
    suspend fun getDeliveryById(id: Long): DeliveryOrder?

    @Query("SELECT COUNT(*) FROM deliveries")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDelivery(delivery: DeliveryOrder): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(deliveries: List<DeliveryOrder>)

    @Update
    suspend fun updateDelivery(delivery: DeliveryOrder)

    @Query("UPDATE deliveries SET status = :status, updatedTimestamp = :timestamp WHERE id = :id")
    suspend fun updateStatus(id: Long, status: String, timestamp: Long)

    @Delete
    suspend fun deleteDelivery(delivery: DeliveryOrder)

    @Query("DELETE FROM deliveries")
    suspend fun deleteAllDeliveries()
}
