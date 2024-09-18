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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            Screen()
        }
    }

}

@Composable
fun Screen() {

    val scrollState = rememberScrollState()
    val tasks = remember { mutableStateListOf(Triple("aa", false, false),
        Triple("bb", true, false),
        Triple("cc", false, false)) }

    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .fillMaxHeight(0.1f),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = "Список дел",
                fontSize = 30.sp
            )
        }

        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.7f)
                .border(
                    width = 2.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(8.dp)
                )
                .verticalScroll(state = scrollState),
        ) {

            for (i in 0 until tasks.size) {
                CreateTask(tasks[i], i)
            }
            
        }

        Row(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .fillMaxHeight(0.2f),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            Button(
                onClick = { tasks.add(Triple("Текст дела", false, false)) },
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                ),
                modifier = Modifier.width(150.dp)
            ) {
                Text(text = "Добавить дело")
            }

            SaveButton()
        }

    }
}


@Composable
fun SaveButton() {
    val context = LocalContext.current

    Button(
        onClick = { save(context) },
        shape = RoundedCornerShape(15.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black
        ),
        modifier = Modifier.width(150.dp)
    ) {
        Text(text = "Сохранить")
    }
}

fun save(context: Context) {
    Toast.makeText(context, "Сохранено!", Toast.LENGTH_SHORT).show()
}

@Composable
fun CreateTask(task: Triple<String, Boolean, Boolean>, index: Int = 0) {
    var checked by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(task.first) }

    Row(modifier = Modifier.padding(10.dp)
        .border(
            width = 2.dp,
            color = Black,
            shape = RoundedCornerShape(8.dp)
        ).padding(10.dp)) {

        if (task.second) {

            if (!task.third) {
                TextField(
                    modifier = Modifier
                        .background(color = White)
                        .width(225.dp)
                        .padding(5.dp),
                    value = text,
                    onValueChange = { text = it },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = White,
                        focusedContainerColor = LightGray,
                        unfocusedBorderColor = Gray, focusedBorderColor = Black),
                    singleLine = true,
                    readOnly = true
                )
            }
            else {
                TextField(
                    modifier = Modifier
                        .background(color = Color.White)
                        .width(225.dp)
                        .padding(5.dp),
                    value = text,
                    onValueChange = { text = it },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = White,
                        focusedContainerColor = LightGray,
                        unfocusedBorderColor = Gray, focusedBorderColor = Black),
                    singleLine = true,
                )
            }

        }
        else {
            if (!task.third) {
                TextField(
                    modifier = Modifier
                        .background(color = White)
                        .width(225.dp)
                        .padding(5.dp),
                    value = text,
                    onValueChange = { text = it },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = White,
                        focusedContainerColor = LightGray,
                        unfocusedBorderColor = Gray, focusedBorderColor = Black),
                    singleLine = true,
                    readOnly = true
                )
            }
            else {
                TextField(
                    modifier = Modifier
                        .background(color = White)
                        .width(225.dp)
                        .padding(5.dp),
                    value = text,
                    onValueChange = { text = it },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = White,
                        focusedContainerColor = LightGray,
                        unfocusedBorderColor = Gray, focusedBorderColor = Black),
                    singleLine = true,
                )
            }

        }


        Image(modifier = Modifier
            .clickable { }
            .size(50.dp),
            painter = painterResource(id = R.drawable.edit),
            contentDescription = "Button Image")

        Image(modifier = Modifier
            .clickable { }
            .size(50.dp),
            painter = painterResource(id = R.drawable.delete),
            contentDescription = "Button Image")

        Checkbox(
            checked = checked,
            onCheckedChange = { checked = it },
            colors = CheckboxDefaults.colors(checkmarkColor = White,
                checkedColor = Black)
        )

    }

}