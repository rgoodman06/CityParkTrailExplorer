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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
    var searchText by remember { mutableStateOf("") }

    val parks = listOf(
        "Cascades Park",
        "Tom Brown Park",
        "Lake Ella",
        "Alfred B. Maclay Gardens"
    )

    val filteredParks = parks.filter {
        it.contains(searchText, ignoreCase = true)
    }

    Column(modifier = modifier.padding(16.dp)) {
        Text(text = "Welcome to $name!")

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Explore Tallahassee Parks & Trails")

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Search parks") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        filteredParks.forEach { park ->
            Text(text = park)
        }

        if (filteredParks.isEmpty()) {
            Text(text = "No parks found")
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CityParkTrailExplorerTheme {
        Greeting("Android")
    }
}