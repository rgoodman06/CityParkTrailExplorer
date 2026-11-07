package com.example.cityparktrailexplorer
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
    private fun loadParksFromCSV(): List<String> {
        return try {
            assets.open("parks_data.csv").bufferedReader().use { reader ->
                reader.readLine() // Skip column headings
                reader.lineSequence()
                    .filter { it.isNotBlank() }
                    .map { line ->
                        line.substringBefore(",").trim().trim('"')
                    }
                    .filter { it.isNotBlank() }
                    .toList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val csvParks = loadParksFromCSV()
        setContent {
            CityParkTrailExplorerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "City Park & Trail Explorer",
                        parks = csvParks,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}



@Composable
fun Greeting(
    name: String,
    parks: List<String>,
    modifier: Modifier = Modifier
) {

    var searchText by remember { mutableStateOf("") }
    val parkAmenities = mapOf(
        "Cascades Park" to listOf("Walking Trails", "Picnic Areas"),
        "Tom Brown Park" to listOf("Walking Trails", "Playgrounds", "Picnic Areas"),
        "Lake Ella" to listOf("Walking Trails", "Picnic Areas"),
        "Alfred B. Maclay Gardens" to listOf("Walking Trails", "Picnic Areas")
    )

    var selectedAmenity by remember { mutableStateOf("All") }
    val filteredParks = parks.filter { park ->
        val matchesSearch = park.contains(searchText, ignoreCase = true)
        val matchesAmenity = selectedAmenity == "All" ||
                parkAmenities[park]?.contains(selectedAmenity) == true

        matchesSearch && matchesAmenity
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

        Text(text = "Filter by amenity")

        var menuExpanded by remember { mutableStateOf(false) }

        Box {
            OutlinedButton(onClick = { menuExpanded = true }) {
                Text(text = selectedAmenity)
            }

            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                listOf("All", "Walking Trails", "Playgrounds", "Picnic Areas")
                    .forEach { amenity ->
                        DropdownMenuItem(
                            text = { Text(amenity) },
                            onClick = {
                                selectedAmenity = amenity
                                menuExpanded = false
                            }
                        )
                    }
            }
        }

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
        Greeting(
            name = "Android",
            parks = listOf("Sample Park"),
            modifier = Modifier
        )
    }
}