package com.bukerabrothers.easyinventory.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.bukerabrothers.easyinventory.viewmodel.InventoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockMovementScreen(
    viewModel: InventoryViewModel,
    productId: Long,
    onDone: () -> Unit
) {
    var qty by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    Scaffold(topBar = { TopAppBar(title = { Text("Stock In / Out") }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            OutlinedTextField(
                qty, { qty = it }, label = { Text("Quantity") },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(note, { note = it }, label = { Text("Note (optional)") }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp))

            Row(Modifier.fillMaxWidth().padding(top = 16.dp)) {
                Button(onClick = {
                    val q = qty.toIntOrNull() ?: 0
                    if (q > 0) {
                        viewModel.recordStockIn(productId, q, note.ifBlank { null })
                        onDone()
                    }
                }, modifier = Modifier.padding(end = 8.dp)) { Text("Stock In") }

                Button(onClick = {
                    val q = qty.toIntOrNull() ?: 0
                    if (q > 0) {
                        viewModel.recordStockOut(productId, q, note.ifBlank { null })
                        onDone()
                    }
                }) { Text("Stock Out") }
            }
        }
    }
}
