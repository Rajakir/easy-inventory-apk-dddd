package com.bukerabrothers.easyinventory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.bukerabrothers.easyinventory.data.AppDatabase
import com.bukerabrothers.easyinventory.repository.InventoryRepository
import com.bukerabrothers.easyinventory.ui.nav.EasyInventoryNavGraph
import com.bukerabrothers.easyinventory.ui.theme.EasyInventoryTheme
import com.bukerabrothers.easyinventory.viewmodel.InventoryViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: InventoryViewModel by viewModels {
        val db = AppDatabase.getInstance(applicationContext)
        InventoryViewModel.Factory(InventoryRepository(db))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EasyInventoryTheme {
                EasyInventoryNavGraph(viewModel = viewModel)
            }
        }
    }
}
