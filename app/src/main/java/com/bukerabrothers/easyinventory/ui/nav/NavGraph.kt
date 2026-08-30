package com.bukerabrothers.easyinventory.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bukerabrothers.easyinventory.ui.screens.AddEditProductScreen
import com.bukerabrothers.easyinventory.ui.screens.ProductListScreen
import com.bukerabrothers.easyinventory.ui.screens.StockMovementScreen
import com.bukerabrothers.easyinventory.viewmodel.InventoryViewModel

@Composable
fun EasyInventoryNavGraph(viewModel: InventoryViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            ProductListScreen(
                viewModel = viewModel,
                onAddProduct = { navController.navigate("add") },
                onOpenProduct = { id -> navController.navigate("move/$id") }
            )
        }
        composable("add") {
            AddEditProductScreen(viewModel = viewModel, existing = null) {
                navController.popBackStack()
            }
        }
        composable(
            "move/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.LongType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getLong("productId") ?: 0L
            StockMovementScreen(viewModel = viewModel, productId = productId) {
                navController.popBackStack()
            }
        }
    }
}
