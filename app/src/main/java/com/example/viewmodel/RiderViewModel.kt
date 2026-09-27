package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.DeliveryOrder
import com.example.data.DeliveryRepository
import com.example.util.AppLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class RiderUiState(
    val orders: List<DeliveryOrder> = emptyList(),
    val filteredOrders: List<DeliveryOrder> = emptyList(),
    val statusFilter: String? = null,
    val searchQuery: String = "",
    val language: AppLanguage = AppLanguage.EN,
    val isTableView: Boolean = false,
    val selectedOrder: DeliveryOrder? = null,
    val isAddDialogOpen: Boolean = false,
    val totalCount: Int = 0,
    val pendingCount: Int = 0,
    val outForDeliveryCount: Int = 0,
    val deliveredCount: Int = 0,
    val failedCount: Int = 0,
    val totalCodAmount: Double = 0.0
)

class RiderViewModel(
    private val repository: DeliveryRepository
) : ViewModel() {

    private val _statusFilter = MutableStateFlow<String?>(null)
    private val _searchQuery = MutableStateFlow("")
    private val _language = MutableStateFlow(AppLanguage.EN)
    private val _isTableView = MutableStateFlow(false)
    private val _selectedOrder = MutableStateFlow<DeliveryOrder?>(null)
    private val _isAddDialogOpen = MutableStateFlow(false)

    private data class UiControls(
        val isTableView: Boolean,
        val selectedOrder: DeliveryOrder?,
        val isAddDialogOpen: Boolean
    )

    private val _uiControls = combine(
        _isTableView,
        _selectedOrder,
        _isAddDialogOpen
    ) { isTable, selected, isAddOpen ->
        UiControls(isTable, selected, isAddOpen)
    }

    val uiState: StateFlow<RiderUiState> = combine(
        repository.allDeliveries,
        _statusFilter,
        _searchQuery,
        _language,
        _uiControls
    ) { allOrders: List<DeliveryOrder>, filter: String?, query: String, lang: AppLanguage, controls: UiControls ->
        val filtered = allOrders.filter { order ->
            val matchesFilter = when {
                filter == null -> true
                filter == "Pending" -> order.status.equals("Pending", ignoreCase = true)
                filter == "Out for delivery" -> order.status.contains("out", ignoreCase = true)
                filter == "Delivered" -> order.status.contains("deliver", ignoreCase = true) && !order.status.contains("out", ignoreCase = true)
                filter == "Failed" -> order.status.contains("fail", ignoreCase = true) || order.status.contains("cancel", ignoreCase = true)
                else -> true
            }

            val matchesQuery = if (query.isBlank()) {
                true
            } else {
                order.orderId.contains(query, ignoreCase = true) ||
                        order.customerName.contains(query, ignoreCase = true) ||
                        order.address.contains(query, ignoreCase = true)
            }

            matchesFilter && matchesQuery
        }

        val pending = allOrders.count { it.status.equals("Pending", ignoreCase = true) }
        val outForDelivery = allOrders.count { it.status.contains("out", ignoreCase = true) }
        val delivered = allOrders.count { it.status.contains("deliver", ignoreCase = true) && !it.status.contains("out", ignoreCase = true) }
        val failed = allOrders.count { it.status.contains("fail", ignoreCase = true) || it.status.contains("cancel", ignoreCase = true) }
        val codSum = allOrders.filter { !it.isPrepaid && !it.status.contains("deliver", ignoreCase = true) }.sumOf { it.codAmount }

        RiderUiState(
            orders = allOrders,
            filteredOrders = filtered,
            statusFilter = filter,
            searchQuery = query,
            language = lang,
            isTableView = controls.isTableView,
            selectedOrder = controls.selectedOrder,
            isAddDialogOpen = controls.isAddDialogOpen,
            totalCount = allOrders.size,
            pendingCount = pending,
            outForDeliveryCount = outForDelivery,
            deliveredCount = delivered,
            failedCount = failed,
            totalCodAmount = codSum
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RiderUiState()
    )

    fun setStatusFilter(filter: String?) {
        _statusFilter.value = filter
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleLanguage() {
        _language.value = when (_language.value) {
            AppLanguage.EN -> AppLanguage.HI
            AppLanguage.HI -> AppLanguage.ES
            AppLanguage.ES -> AppLanguage.EN
        }
    }

    fun setLanguage(lang: AppLanguage) {
        _language.value = lang
    }

    fun toggleViewMode() {
        _isTableView.value = !_isTableView.value
    }

    fun selectOrder(order: DeliveryOrder?) {
        _selectedOrder.value = order
    }

    fun setAddDialogOpen(open: Boolean) {
        _isAddDialogOpen.value = open
    }

    fun updateOrderStatus(orderId: Long, newStatus: String) {
        viewModelScope.launch {
            repository.updateStatus(orderId, newStatus)
            if (_selectedOrder.value?.id == orderId) {
                _selectedOrder.value = repository.getDeliveryById(orderId)
            }
        }
    }

    fun addNewDelivery(
        orderNumber: String,
        customerName: String,
        customerPhone: String,
        address: String,
        latitude: Double?,
        longitude: Double?,
        status: String,
        packageType: String,
        codAmount: Double,
        isPrepaid: Boolean,
        instructions: String
    ) {
        viewModelScope.launch {
            val formattedOrderId = if (orderNumber.startsWith("#")) orderNumber else "#$orderNumber"
            val newOrder = DeliveryOrder(
                orderId = formattedOrderId,
                customerName = customerName.trim(),
                customerPhone = customerPhone.trim(),
                address = address.trim(),
                latitude = latitude,
                longitude = longitude,
                status = status,
                packageType = packageType.ifBlank { "Standard Parcel" },
                codAmount = codAmount,
                isPrepaid = isPrepaid,
                instructions = instructions.trim()
            )
            repository.insertDelivery(newOrder)
            _isAddDialogOpen.value = false
        }
    }

    fun deleteDelivery(order: DeliveryOrder) {
        viewModelScope.launch {
            repository.deleteDelivery(order)
            if (_selectedOrder.value?.id == order.id) {
                _selectedOrder.value = null
            }
        }
    }

    fun resetToDemoData() {
        viewModelScope.launch {
            repository.ensureDefaultData()
        }
    }
}

class RiderViewModelFactory(
    private val repository: DeliveryRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RiderViewModel::class.java)) {
            return RiderViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
