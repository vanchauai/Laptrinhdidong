package com.example.lab2

import android.media.Image
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab2.ui.theme.Lab2Theme
import java.nio.file.WatchEvent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab2Theme {
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column( verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) { ShowBusinessCard(name = "Nguyen Van Chau",
                        title = "Sinh vien: 24AI007")
                        Infomation()
                    }

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BusinessCardPreview() {
    Lab2Theme {
        ShowBusinessCard("Nguyen Van Chau", "Sinh vien: 24AI007")
    }
}

@Composable
fun NameText(name: String, title: String, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = name,
            fontSize = 30.sp,
            lineHeight = 10.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = title,
            fontSize = 20.sp,
            lineHeight = 10.sp,
            textAlign = TextAlign.Center
        )
    }
}



@Composable
fun Infomation(modifier: Modifier = Modifier) {
    val phoneimage = painterResource(R.drawable.phone)
    val mailimage = painterResource(R.drawable.email)
    val shareimage = painterResource(R.drawable.share)
    @Composable
    fun Showrow(message: String,painter: Painter, modifier: Modifier = Modifier) {
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = modifier) {
            Image(painter = painter,
                contentDescription = null,
                modifier = Modifier.padding(end = 10.dp, bottom = 15.dp).size(30.dp))
            Text(text = message,
                fontSize = 20.sp,
                lineHeight = 10.sp,
                textAlign = TextAlign.Left,
                modifier = Modifier.padding(bottom = 15.dp))
        }
    }
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.padding(bottom = 8.dp, top = 150.dp)
    ) {
        Showrow("0984686724", phoneimage)
        Showrow("VKU", shareimage)
        Showrow("chaunv.24ai@vku.udn.vn", mailimage)
    }
}

@Composable
fun ShowBusinessCard(name: String,title: String, modifier: Modifier = Modifier) {
    val image = painterResource(R.drawable.avatar)
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(8.dp)
    ) {
            Image(painter = image,
                contentDescription = null,
                modifier = Modifier.padding(bottom = 20.dp)
                )
            NameText(name = name,
                title = title,
                modifier = Modifier.padding(8.dp))
        }
}