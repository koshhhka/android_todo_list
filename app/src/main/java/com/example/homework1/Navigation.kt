package com.example.homework1

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation(
    startDestination: String = "list"
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val fileStorage = remember { FileStorage(context) }
    
    var refreshKey by remember { mutableStateOf(0) }

    NavHost(navController = navController, startDestination = startDestination) {
        composable("list") {
            TodoListScreen(
                fileStorage = fileStorage,
                onAddClick = { navController.navigate("edit_new") },
                onEditClick = { id -> navController.navigate("edit/$id") },
                refreshKey = refreshKey
            )
        }
        composable("edit_new") {
            EditTodoScreen(
                initial = null,
                onSave = { item ->
                    fileStorage.add(item)
                    fileStorage.saveToFile()
                    refreshKey++
                    navController.popBackStack()
                },
                onCancel = { navController.popBackStack() }
            )
        }
        composable("edit/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            val item = id?.let { fileStorage.getItem(it) }
            EditTodoScreen(
                initial = item,
                onSave = { savedItem ->
                    if (item != null) {
                        fileStorage.update(savedItem)
                    } else {
                        fileStorage.add(savedItem)
                    }
                    fileStorage.saveToFile()
                    refreshKey++
                    navController.popBackStack()
                },
                onCancel = { navController.popBackStack() }
            )
        }
    }
}