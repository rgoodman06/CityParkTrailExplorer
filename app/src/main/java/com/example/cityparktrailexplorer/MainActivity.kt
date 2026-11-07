package com.example.cityparktrailexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cityparktrailexplorer.ui.theme.CityParkTrailExplorerTheme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CityParkTrailExplorerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "City Park & Trail Explorer",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(text = "Welcome to $name!")

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Explore Tallahassee Parks & Trails")

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "1. Cascades Park")
        Text(text = "2. Tom Brown Park")
        Text(text = "3. Lake Ella")
        Text(text = "4. Alfred B. Maclay Gardens")
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CityParkTrailExplorerTheme {
        Greeting("Android")
    }
}