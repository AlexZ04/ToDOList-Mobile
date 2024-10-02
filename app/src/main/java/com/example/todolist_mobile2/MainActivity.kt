package com.example.todolist_mobile2

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist_mobile2.ui.theme.ToDoListMobile2Theme
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.io.File

val BASE_URL = "https://59ce-79-136-243-76.ngrok-free.app"

val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val messagesApi = retrofit.create(TaskApi::class.java)

var editingTask = 0
var editingText = ""
var editingComp = false

class MainActivity : ComponentActivity() {

    companion object {
        var tasksAmount = 0
    }

    private val tasks = mutableStateListOf<Task>()

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {
            ToDoListMobile2Theme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {

                    fetchTasks()

                    Screen(tasks, this)
                }
            }
        }

    }

    fun fetchTasks() {
        val call = messagesApi.fetchData()

        call.enqueue(object : Callback<List<Note>> {
            override fun onResponse(call: Call<List<Note>>, response: Response<List<Note>>) {
                if (response.isSuccessful) {
                    val data = response.body() ?: listOf()

                    tasks.clear()
                    for (i in data.indices) {
                        tasks.add(Task(data[i].text, data[i].isCompleted, data[i].id))
                    }

                } else {
                    Log.d("MyLog", "Request failed with code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Note>>, t: Throwable) {
                Log.d("MyLog", "Network request failed", t)
            }

        })

    }

}


data class Task(
    @SerializedName("text") var text: String,
    @SerializedName("isCompleted") var isCompleted: Boolean,
    @SerializedName("number") var number: Number
)

data class Note(
    var id: Number,
    var text: String,
    var isCompleted: Boolean
)

interface TaskApi {
    @GET("Tasks")
    fun fetchData(): Call<List<Note>>
    @POST("Tasks")
    fun createTask(@Body newTask: Note): Call<Note>
    @DELETE("Tasks/{id}")
    fun deleteTask(@Path("id") id: Int): Call<Void>
    @PATCH("Tasks/{id}")
    fun checkTask(@Path("id") id: Int): Call<Void>
    @PUT("Tasks/{id}")
    fun changeTask(@Path("id") id: Number, @Body newTask: Note): Call<Void>
}

@Composable
fun Screen(tasks: MutableList<Task>, context: Context) {
    val focusManager = LocalFocusManager.current

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

                    focusManager.clearFocus()
                    changeTaskText()

                    val call =
                        messagesApi.createTask(Note(MainActivity.tasksAmount - 1, "Дело",
                            false))
                    call.enqueue(object : Callback<Note> {
                        override fun onResponse(call: Call<Note>, response: Response<Note>) {
                            if (!response.isSuccessful) {
                                Log.d("MyLog", "Error with creating task: ${response.code()}")
                            }
                            else {
                                val data = response.body()

                                if (data != null) {
                                    tasks.add(
                                        Task(
                                            "Дело", false,
                                            data.id
                                        )
                                    )
                                }

                                MainActivity.tasksAmount++
                            }
                        }

                        override fun onFailure(call: Call<Note>, t: Throwable) {
                            Log.d("MyLog", "Error with creating task", t)
                        }
                    })
                },
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Black
                ),
                modifier = Modifier.width(120.dp)
            ) {
                Text(text = "Добавить")
            }

        }

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
                    editingTask = task.number.toInt()
                    editingText = it
                    editingComp = task.isCompleted
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                    changeTaskText()
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
                    editingTask = task.number.toInt()
                    editingText = it
                    editingComp = task.isCompleted
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
                    changeTaskText()
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
                focusManager.clearFocus()

                val call =
                    messagesApi.deleteTask(task.number.toInt())
                call.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            tasks.remove(task)
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.e("MainActivity", "Failed to delete task", t)
                        t.printStackTrace()
                    }
                })
            }
            .size(50.dp),
            painter = painterResource(id = R.drawable.delete),
            contentDescription = "Delete button")

        Checkbox(
            checked = checked,
            onCheckedChange = {
                focusManager.clearFocus()
                changeTaskText()

                val call =
                    messagesApi.checkTask(task.number.toInt())
                call.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            checked = it
                            for (i in 0 until tasks.size) {
                                if (task.number == tasks[i].number) {
                                    tasks[i].isCompleted = it
                                    break
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.e("MainActivity", "Failed to rename task", t)
                        t.printStackTrace()
                    }
                })

            },
            colors = CheckboxDefaults.colors(
                checkmarkColor = White,
                checkedColor = Black
            )
        )

    }

}

fun changeTaskText() {
    val call =
        messagesApi.changeTask(editingTask,
            Note(editingTask, editingText, editingComp))
    call.enqueue(object : Callback<Void> {
        override fun onResponse(call: Call<Void>, response: Response<Void>) {
            if (!response.isSuccessful) {
                Log.d("MyLog", "Error with editing task: ${response.code()}")
            }
        }

        override fun onFailure(call: Call<Void>, t: Throwable) {
            Log.d("MyLog", "Ошибка сети при создании задачи", t)
        }
    })
}
