package com.bukerabrothers.easyinventory.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.bukerabrothers.easyinventory.data.Product
import com.bukerabrothers.easyinventory.viewmodel.InventoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductScreen(
    viewModel: InventoryViewModel,
    existing: Product? = null,
    onDone: () -> Unit
) {
    var name by remember { mutableStateOf(existing?.name ?: "") }
    var sku by remember { mutableStateOf(existing?.sku ?: "") }
    var quantity by remember { mutableStateOf((existing?.quantity ?: 0).toString()) }
    var unitPrice by remember { mutableStateOf((existing?.unitPrice ?: 0.0).toString()) }
    var lowStock by remember { mutableStateOf((existing?.lowStockThreshold ?: 5).toString()) }
    var notes by remember { mutableStateOf(existing?.notes ?: "") }

    Scaffold(
        topBar = { TopAppBar(title = { Text(if (existing == null) "Add Product" else "Edit Product") }) }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            OutlinedTextField(name, { name = it }, label = { Text("Product name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(sku, { sku = it }, label = { Text("SKU / Barcode") }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
            OutlinedTextField(
                quantity, { quantity = it }, label = { Text("Quantity") },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )
            OutlinedTextField(
                unitPrice, { unitPrice = it }, label = { Text("Unit price (₹)") },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )
            OutlinedTextField(
                lowStock, { lowStock = it }, label = { Text("Low stock threshold") },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )
            OutlinedTextField(notes, { notes = it }, label = { Text("Notes") }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp))

            Button(
                onClick = {
                    val product = Product(
                        id = existing?.id ?: 0,
                        name = name.trim(),
                        sku = sku.trim().ifBlank { null },
                        quantity = quantity.toIntOrNull() ?: 0,
                        unitPrice = unitPrice.toDoubleOrNull() ?: 0.0,
                        lowStockThreshold = lowStock.toIntOrNull() ?: 5,
                        notes = notes.trim().ifBlank { null }
                    )
                    if (existing == null) viewModel.saveProduct(product) else viewModel.updateProduct(product)
                    onDone()
                },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                Text(if (existing == null) "Add product" else "Save changes")
            }
        }
    }
}
