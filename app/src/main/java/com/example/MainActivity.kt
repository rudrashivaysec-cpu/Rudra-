package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AddDeliveryDialog
import com.example.ui.components.DeliveryCardsView
import com.example.ui.components.DeliveryDetailSheet
import com.example.ui.components.DeliveryTableView
import com.example.ui.components.RiderHeader
import com.example.ui.components.RiderMetricsCard
import com.example.ui.theme.RiderSecondary
import com.example.ui.theme.RiderTrackTheme
import com.example.viewmodel.RiderViewModel
import com.example.viewmodel.RiderViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: RiderViewModel by viewModels {
        val app = application as RiderTrackApp
        RiderViewModelFactory(app.repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RiderTrackTheme {
                RiderTrackMainScreen(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiderTrackMainScreen(viewModel: RiderViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            RiderHeader(
                language = uiState.language,
                isTableView = uiState.isTableView,
                onLanguageClick = { viewModel.toggleLanguage() },
                onToggleView = { viewModel.toggleViewMode() },
                onAddClick = { viewModel.setAddDialogOpen(true) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.setAddDialogOpen(true) },
                containerColor = RiderSecondary,
                contentColor = androidx.compose.ui.graphics.Color.White,
                modifier = Modifier
                    .testTag("fab_add_delivery")
                    .navigationBarsPadding()
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Delivery"
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 900.dp)
                    .padding(horizontal = 14.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Metrics and Filter Section
                item {
                    RiderMetricsCard(
                        uiState = uiState,
                        onFilterSelect = { viewModel.setStatusFilter(it) },
                        onSearchChange = { viewModel.setSearchQuery(it) }
                    )
                }

                // Table View or Cards View based on toggle
                item {
                    if (uiState.isTableView) {
                        DeliveryTableView(
                            orders = uiState.filteredOrders,
                            language = uiState.language,
                            onOrderClick = { viewModel.selectOrder(it) }
                        )
                    } else {
                        DeliveryCardsView(
                            orders = uiState.filteredOrders,
                            language = uiState.language,
                            onOrderClick = { viewModel.selectOrder(it) }
                        )
                    }
                }
            }
        }

        // Delivery Detail Modal Sheet
        uiState.selectedOrder?.let { selected ->
            DeliveryDetailSheet(
                order = selected,
                language = uiState.language,
                sheetState = sheetState,
                onDismiss = { viewModel.selectOrder(null) },
                onStatusChange = { newStatus ->
                    viewModel.updateOrderStatus(selected.id, newStatus)
                    scope.launch {
                        snackbarHostState.showSnackbar("Status updated to $newStatus")
                    }
                },
                onDelete = {
                    viewModel.deleteDelivery(selected)
                    scope.launch {
                        sheetState.hide()
                        viewModel.selectOrder(null)
                        snackbarHostState.showSnackbar("Order removed from manifest")
                    }
                }
            )
        }

        // Add Delivery Dialog
        if (uiState.isAddDialogOpen) {
            AddDeliveryDialog(
                language = uiState.language,
                onDismiss = { viewModel.setAddDialogOpen(false) },
                onConfirm = { orderNumber, name, phone, addr, lat, lng, st, pkg, cod, prepaid, note ->
                    viewModel.addNewDelivery(
                        orderNumber = orderNumber,
                        customerName = name,
                        customerPhone = phone,
                        address = addr,
                        latitude = lat,
                        longitude = lng,
                        status = st,
                        packageType = pkg,
                        codAmount = cod,
                        isPrepaid = prepaid,
                        instructions = note
                    )
                    scope.launch {
                        snackbarHostState.showSnackbar("Order $orderNumber added to delivery manifest")
                    }
                }
            )
        }
    }
}
