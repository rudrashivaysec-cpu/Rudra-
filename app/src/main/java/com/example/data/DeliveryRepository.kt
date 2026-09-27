package com.example.data

import kotlinx.coroutines.flow.Flow

class DeliveryRepository(private val deliveryDao: DeliveryDao) {

    val allDeliveries: Flow<List<DeliveryOrder>> = deliveryDao.getAllDeliveries()

    fun getDeliveriesByStatus(status: String): Flow<List<DeliveryOrder>> {
        return deliveryDao.getDeliveriesByStatus(status)
    }

    suspend fun getDeliveryById(id: Long): DeliveryOrder? {
        return deliveryDao.getDeliveryById(id)
    }

    suspend fun insertDelivery(delivery: DeliveryOrder): Long {
        return deliveryDao.insertDelivery(delivery)
    }

    suspend fun updateDelivery(delivery: DeliveryOrder) {
        deliveryDao.updateDelivery(delivery)
    }

    suspend fun updateStatus(id: Long, status: String) {
        deliveryDao.updateStatus(id, status, System.currentTimeMillis())
    }

    suspend fun deleteDelivery(delivery: DeliveryOrder) {
        deliveryDao.deleteDelivery(delivery)
    }

    suspend fun ensureDefaultData() {
        if (deliveryDao.getCount() == 0) {
            AppDatabase.populateInitialData(deliveryDao)
        }
    }
}
