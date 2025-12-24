package com.example.homework1

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.homework1.data.local.LocalDataSource
import com.example.homework1.data.local.db.TodoDatabase
import com.example.homework1.data.remote.AuthManager
import com.example.homework1.data.remote.RemoteDataSource
import com.example.homework1.domain.repository.TodoRepository
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(
    startDestination: String = "list"
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    val database = remember { TodoDatabase.getDatabase(context) }
    val localDataSource = remember { LocalDataSource(database) }
    val authManager = remember { AuthManager(context) }
    val remoteDataSource = remember { 
        authManager.initializeToken()
        RemoteDataSource() 
    }
    val repository = remember { TodoRepository(localDataSource, remoteDataSource) }

    NavHost(navController = navController, startDestination = startDestination) {
        composable("list") {
            TodoListScreen(
                repository = repository,
                onAddClick = { navController.navigate("edit_new") },
                onEditClick = { id -> navController.navigate("edit/$id") }
            )
        }
        composable("edit_new") {
            EditTodoScreen(
                itemId = null,
                repository = null,
                initial = null,
                onSave = { item ->
                    scope.launch {
                        repository.saveItem(item)
                        navController.popBackStack()
                    }
                },
                onCancel = { navController.popBackStack() }
            )
        }
        composable("edit/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            
            EditTodoScreen(
                itemId = id,
                repository = repository,
                onSave = { savedItem ->
                    scope.launch {
                        repository.saveItem(savedItem)
                        navController.popBackStack()
                    }
                },
                onCancel = { navController.popBackStack() }
            )
        }
    }
}