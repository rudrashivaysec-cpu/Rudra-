package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.RiderPrimary
import com.example.ui.theme.RiderSecondary
import com.example.util.AppLanguage
import com.example.util.StringsHelper

@Composable
fun AddDeliveryDialog(
    language: AppLanguage,
    onDismiss: () -> Unit,
    onConfirm: (
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
    ) -> Unit
) {
    var orderNumber by remember { mutableStateOf("#10249") }
    var customerName by remember { mutableStateOf("") }
    var customerPhone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var latText by remember { mutableStateOf("") }
    var lngText by remember { mutableStateOf("") }
    var packageType by remember { mutableStateOf("Standard Parcel") }
    var isPrepaid by remember { mutableStateOf(true) }
    var codText by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("dialog_add_delivery")
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = StringsHelper.get("add_delivery", language),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = RiderPrimary
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
                    }
                }

                if (errorMessage != null) {
                    Text(
                        text = errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                OutlinedTextField(
                    value = orderNumber,
                    onValueChange = { orderNumber = it },
                    label = { Text("Order ID") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = customerName,
                    onValueChange = { customerName = it },
                    label = { Text("Customer Name *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = customerPhone,
                    onValueChange = { customerPhone = it },
                    label = { Text("Phone Number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Delivery Address *") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = latText,
                        onValueChange = { latText = it },
                        label = { Text("Latitude (opt)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = lngText,
                        onValueChange = { lngText = it },
                        label = { Text("Longitude (opt)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = packageType,
                    onValueChange = { packageType = it },
                    label = { Text("Package Description") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isPrepaid) "Prepaid Order" else "Cash On Delivery (COD)",
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp
                    )
                    Switch(
                        checked = isPrepaid,
                        onCheckedChange = { isPrepaid = it }
                    )
                }

                if (!isPrepaid) {
                    OutlinedTextField(
                        value = codText,
                        onValueChange = { codText = it },
                        label = { Text("COD Amount to Collect (₹)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                OutlinedTextField(
                    value = instructions,
                    onValueChange = { instructions = it },
                    label = { Text("Delivery Instructions / Notes") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            if (customerName.isBlank() || address.isBlank()) {
                                errorMessage = "Customer name and address are required"
                                return@Button
                            }
                            val lat = latText.toDoubleOrNull()
                            val lng = lngText.toDoubleOrNull()
                            val cod = codText.toDoubleOrNull() ?: 0.0

                            onConfirm(
                                orderNumber,
                                customerName,
                                customerPhone,
                                address,
                                lat,
                                lng,
                                "Pending",
                                packageType,
                                cod,
                                isPrepaid,
                                instructions
                            )
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RiderPrimary)
                    ) {
                        Text("Add Order", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
