package com.example.homework1

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation(
    startDestination: String = "list"
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        composable("list") {
            TodoListScreen(
                onAddClick = { navController.navigate("edit_new") },
                onEditClick = { id -> navController.navigate("edit/$id") }
            )
        }
        composable("edit_new") {
            EditTodoScreen(onSave = { /* сохранить */ navController.popBackStack() }, onCancel = { navController.popBackStack() })
        }
        composable("edit/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            // Здесь можно подгрузить данные по id
            EditTodoScreen(onSave = { navController.popBackStack() }, onCancel = { navController.popBackStack() })
        }
    }
}