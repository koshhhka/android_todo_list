package com.example.homework1

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import java.time.LocalDateTime
import java.util.*

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(
    onAddClick: () -> Unit,
    onEditClick: (String) -> Unit
) {
    var todos by remember {
        mutableStateOf(
            mutableListOf(
                TodoItem(
                    uid = UUID.randomUUID().toString(),
                    text = "Купить молоко",
                    importance = Importance.REGULAR,
                    color = android.graphics.Color.YELLOW,
                    deadLine = LocalDateTime.now(),
                    isDone = false
                ),
                TodoItem(
                    uid = UUID.randomUUID().toString(),
                    text = "Позвонить маме",
                    importance = Importance.IMPORTANT, // исправлено
                    color = android.graphics.Color.RED,
                    deadLine = null,
                    isDone = true
                )
            )
        )
    }

    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Добавить")
            }
        },
        topBar = {
            TopAppBar(title = { Text("Мои дела") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            items(todos.size) { index ->
                val item = todos[index]

                val dismissState = rememberDismissState { value ->
                    if (value == DismissValue.DismissedToStart) {
                        todos.removeAt(index)
                        true
                    } else false
                }

                SwipeToDismiss(
                    state = dismissState,
                    directions = setOf(DismissDirection.EndToStart),
                    background = {
                        val color = when (dismissState.dismissDirection) {
                            DismissDirection.EndToStart -> Color.Red
                            else -> Color.Transparent
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color)
                                .padding(16.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Text("Delete", color = Color.White)
                        }
                    },
                    dismissContent = {
                        TodoRow(item = item, onClick = { onEditClick(item.uid) })
                    }
                )
            }
        }
    }
}

@Composable
fun TodoRow(item: TodoItem, onClick: () -> Unit) {
    Surface(
        tonalElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(item.text, style = MaterialTheme.typography.titleMedium)
                item.deadLine?.let {
                    Text("До: ${it.toLocalDate()}", style = MaterialTheme.typography.bodySmall)
                }
            }
            Checkbox(checked = item.isDone, onCheckedChange = null)
        }
    }
}
