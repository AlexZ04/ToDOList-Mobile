package com.example.todolist_mobile2

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist_mobile2.ui.theme.ToDoListMobile2Theme
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.io.File


class MainActivity : ComponentActivity() {

    companion object {
        var tasksAmount = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {
            ToDoListMobile2Theme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    val tasks = remember {
                        mutableStateListOf(
                            Task("aa", false, false, 0),
                        )
                    }

                    tasks.clear()
                    tasks.addAll(load(this))

                    Screen(tasks)
                }
            }
        }

    }

}


data class Task(
    @SerializedName("text") var text: String,
    @SerializedName("isCompleted") var isCompleted: Boolean,
    @SerializedName("isEditable") var isEditable: Boolean,
    @SerializedName("number") var number: Number
)

@Composable
fun Screen(tasks: MutableList<Task>) {

    val focusManager = LocalFocusManager.current

    MainActivity.tasksAmount += 3

    Column(
        modifier = Modifier
            .background(White)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .background(White)
                .fillMaxWidth()
                .fillMaxHeight(0.1f),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = "Список дел",
                fontSize = 30.sp
            )
        }

        LazyColumn(
            modifier = Modifier
                .background(White)
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.7f)
                .border(
                    width = 2.dp,
                    color = Black,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {

            items(tasks, key = { task -> task.number }) { task ->
                CreateTask(task, tasks)
            }

        }

        Row(
            modifier = Modifier
                .background(White)
                .fillMaxWidth()
                .fillMaxHeight(0.2f),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            Button(
                onClick = {
                    tasks.add(
                        Task(
                            "Дело", false, true,
                            MainActivity.tasksAmount
                        )
                    )

                    MainActivity.tasksAmount++
                    focusManager.clearFocus()
                },
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Black
                ),
                modifier = Modifier.width(150.dp)
            ) {
                Text(text = "Добавить дело")
            }

            SaveButton(tasks)
        }

    }
}


@Composable
fun SaveButton(tasks: MutableList<Task>) {
    val context = LocalContext.current

    val focusManager = LocalFocusManager.current

    Button(
        onClick = {
            save(context, tasks)
            focusManager.clearFocus()
        },
        shape = RoundedCornerShape(15.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Black
        ),
        modifier = Modifier.width(150.dp)
    ) {
        Text(text = "Сохранить")
    }
}

fun save(context: Context, tasks: MutableList<Task>) {
    Toast.makeText(context, "Сохранено!", Toast.LENGTH_SHORT).show()

    val gson = Gson()
    val json = gson.toJson(tasks)

    val file = File(context.filesDir, "tasks.json")
    file.writeText(json)
}

fun load(context: Context): List<Task> {
    val gson = Gson()
    val file = File(context.filesDir, "tasks.json")

    return if (file.exists()) {
        gson.fromJson(file.readText(), object : TypeToken<List<Task>>() {}.type)
    } else {
        emptyList()
    }
}

@Composable
fun CreateTask(task: Task, tasks: MutableList<Task>) {

    var checked by remember { mutableStateOf(task.isCompleted) }
    var text by remember { mutableStateOf(task.text) }

    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier
            .padding(10.dp)
            .border(
                width = 2.dp,
                color = Black,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(10.dp)
    ) {

        if (task.isCompleted) {

            TextField(
                modifier = Modifier
                    .background(color = White)
                    .width(250.dp)
                    .padding(5.dp),
                value = text,
                onValueChange = {
                    text = it
                    for (i in 0 until tasks.size) {
                        if (task.number == tasks[i].number) {
                            tasks[i].text = text
                            break
                        }
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                }),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Green,
                    focusedContainerColor = LightGray,
                    unfocusedBorderColor = Gray, focusedBorderColor = Black
                ),
                singleLine = true,
            )

        } else {

            TextField(
                modifier = Modifier
                    .background(color = White)
                    .width(250.dp)
                    .padding(5.dp),
                value = text,
                onValueChange = {
                    text = it
                    for (i in 0 until tasks.size) {
                        if (task.number == tasks[i].number) {
                            tasks[i].text = text
                            break
                        }
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                }),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = White,
                    focusedContainerColor = LightGray,
                    unfocusedBorderColor = Gray, focusedBorderColor = Black
                ),
                singleLine = true,
            )

        }

        Image(modifier = Modifier
            .clickable {
                tasks.remove(task)
                focusManager.clearFocus()
            }
            .size(50.dp),
            painter = painterResource(id = R.drawable.delete),
            contentDescription = "Delete button")

        Checkbox(
            checked = checked,
            onCheckedChange = {
                focusManager.clearFocus()
                checked = it
                for (i in 0 until tasks.size) {
                    if (task.number == tasks[i].number) {
                        tasks[i].isCompleted = it
                        break
                    }
                }

            },
            colors = CheckboxDefaults.colors(
                checkmarkColor = White,
                checkedColor = Black
            )
        )

    }

}