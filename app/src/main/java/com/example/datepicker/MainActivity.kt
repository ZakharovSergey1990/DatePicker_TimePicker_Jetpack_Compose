package com.example.datepicker

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.widget.CalendarView
import android.widget.TimePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.datepicker.ui.theme.DatePickerTheme
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DatePickerTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val sdfTime = SimpleDateFormat("HH:mm")
                    val sdfDate = SimpleDateFormat("dd.MM.yyyy")
            val dateText = remember{ mutableStateOf(Calendar.getInstance())}
            val timeText = remember{ mutableStateOf(Calendar.getInstance())}
            val showDialogTime = remember{ mutableStateOf(false)}
            val showDialogDate = remember{ mutableStateOf(false)}
                    Column(modifier = Modifier
                        .padding(50.dp)
                        .fillMaxWidth()) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Button(onClick = { showDialogDate.value=true }) {
                                Text(text = "Date")
                            }
                            Text(text = sdfDate.format(dateText.value.time), fontSize = 22.sp)
                        }
                        Spacer(modifier = Modifier.size(30.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Button(onClick = { showDialogTime.value=true }) {
                                Text(text = "Time",)
                            }
                            Text(text = sdfTime.format(timeText.value.time), fontSize = 22.sp)
                        }
                    }
                  DatePickerCustom(showDialog = showDialogDate.value, onDismissRequest = { showDialogDate.value = false }) {
                      date -> dateText.value = date
                  }
                    TimePickerCustom(showDialog = showDialogTime.value, onDismissRequest = { showDialogTime.value = false }) {
                        time-> timeText.value = time
                    }
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun DatePickerCustom( showDialog: Boolean, onDismissRequest: () -> Unit, onDateSelected: (Calendar) -> Unit ) {
    val theme = LocalContext.current.theme
    val calendar =  remember{ mutableStateOf(Calendar.getInstance())}
    if(showDialog) {
        Dialog(onDismissRequest = { onDismissRequest() }) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .background(
                        color = MaterialTheme.colors.surface,
                        shape = RoundedCornerShape(size = 16.dp)
                    )
            ) {
                AndroidView(
                    modifier = Modifier.wrapContentSize(),
                    factory = { context ->
                        CalendarView(ContextThemeWrapper(context, theme))
                    },
                    update = { view ->
                        view.setOnDateChangeListener { view, year, month, dayOfMonth ->
calendar.value.set(Calendar.YEAR, year)
calendar.value.set(Calendar.MONTH, month)
calendar.value.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        }
                    }
                )
                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(bottom = 16.dp, end = 16.dp)
                ) {
                    TextButton(
                        onClick = onDismissRequest
                    ) {
                        Text(text = "Cancel",)
                    }
                    TextButton(
                        onClick = {
                            onDateSelected(calendar.value)
                            onDismissRequest()
                        }
                    ) { Text(text = "Ok",) }
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun TimePickerCustom(showDialog: Boolean, onDismissRequest:()-> Unit, onTimeSelected: (Calendar)-> Unit){
    val theme = LocalContext.current.theme
    val calendar =  remember{ mutableStateOf(Calendar.getInstance())}
    if(showDialog) {
        Dialog(onDismissRequest = { onDismissRequest() }) {
            Surface(modifier = Modifier.background(Color.White)) {
                Column() {
                    AndroidView(
                        modifier = Modifier.wrapContentSize(),
                        factory = { context ->
                            TimePicker(
                                ContextThemeWrapper(
                                    context,
                                    theme
                                )
                            )
                        },
                        update = { view ->
                            view.setOnTimeChangedListener { view, hourOfDay, minuteOfDay ->
                               calendar.value.set(Calendar.HOUR_OF_DAY, hourOfDay)
                               calendar.value.set(Calendar.MINUTE, minuteOfDay)

                                Log.d("log", "calendar.time = ${calendar.value.time}")
                            }
                        }
                    )
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                      TextButton(onClick = { onTimeSelected(calendar.value)
                          onDismissRequest()
                      }) {
                          Text(text = "Ok")
                      }
                        TextButton(onClick = { onDismissRequest() }) {
                            Text(text = "Cancel")
                        }
                    }
                }
            }
        }
    }
}