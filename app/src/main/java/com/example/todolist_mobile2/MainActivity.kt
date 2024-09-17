package com.example.todolist_mobile2

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist_mobile2.ui.theme.ToDoListMobile2Theme
import java.security.AccessController.getContext

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
                ).verticalScroll(state = scrollState),
        ) {
            Text(
                text = "Пока пусто",
            )
            
        }

        Row(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .fillMaxHeight(0.2f),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            AddTaskButton()

            SaveButton()
        }

    }
}


@Composable
fun SaveButton() {
    val context = LocalContext.current

    Button(
        onClick = { Toast.makeText(context, "Сохранено!", Toast.LENGTH_SHORT).show() },
        shape = RoundedCornerShape(15.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black
        ),
        modifier = Modifier.width(150.dp)
    ) {
        Text(text = "Сохранить")
    }
}

@Composable
fun AddTaskButton() {
    Button(
        onClick = { /*TODO*/ },
        shape = RoundedCornerShape(15.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black
        ),
        modifier = Modifier.width(150.dp)
    ) {
        Text(text = "Добавить дело")
    }
}
