package com.example.tasklistapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tasklistapp.ui.theme.TaskListAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskListAppTheme {
                TaskListScreen()
            }
        }
    }
}

@Composable
fun TaskListScreen() {
    val tasks = remember { mutableStateListOf<Task>() }
    val context = LocalContext.current

    TaskListContent(
        tasks = tasks,
        onAddTask = { title, imageUrl ->
            if (title.isNotEmpty() && imageUrl.isNotEmpty()) {
                tasks.add(Task(title, imageUrl))
            } else {
                Toast.makeText(context, "Please enter both a title and an image URL", Toast.LENGTH_SHORT).show()
            }
        }
    )
}

@Composable
fun TaskListContent(
    tasks: List<Task>,
    onAddTask: (String, String) -> Unit
) {
    var newTaskTitle by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Recipe List", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = newTaskTitle,
            onValueChange = { newTaskTitle = it },
            label = { Text("Recipe Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = imageUrl,
            onValueChange = { imageUrl = it },
            label = { Text("Image URL") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (newTaskTitle.isNotEmpty() && imageUrl.isNotEmpty()) {
                    onAddTask(newTaskTitle, imageUrl)
                    newTaskTitle = ""
                    imageUrl = ""
                }
            }
        ) {
            Text("Add Recipe")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(tasks) { task ->
                TaskItem(task)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun TaskItem(task: Task) {
    var grayscale by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { grayscale = !grayscale },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = task.title, style = MaterialTheme.typography.bodyLarge)

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(task.imageUri)
                .crossfade(true)
                .error(android.R.drawable.ic_menu_report_image) // Imagen predeterminada en caso de error
                .build(),
            contentDescription = null,
            modifier = Modifier.size(64.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TaskListAppTheme {
        TaskListScreen()
    }
}

data class Task(val title: String, val imageUri: String)