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
data class Park(
    val name: String,
    val address: String,
    val amenities: String,
    val trails: Boolean,
    val playground: Boolean,
    val picnicArea: Boolean
)
class MainActivity : ComponentActivity() {

    private fun loadParksFromCSV(): List<Park> {
        return try {
            assets.open("parks_data.csv").bufferedReader().use { reader ->
                val headers = parseCsvLine(reader.readLine() ?: "")
                    .map { it.trim().uppercase() }

                fun field(row: List<String>, column: String): String {
                    val index = headers.indexOf(column)
                    return row.getOrNull(index)?.trim().orEmpty()
                }

                reader.lineSequence()
                    .filter { it.isNotBlank() }
                    .map { line ->
                        val row = parseCsvLine(line)
                        Park(
                            name = field(row, "NAME"),
                            address = field(row, "ADDRESS"),
                            amenities = field(row, "AMENITIES"),
                            trails = field(row, "TRAILS").equals("Y", true),
                            playground = field(row, "PLAYGROUND").equals("Y", true),
                            picnicArea = field(row, "PICNICAREA").equals("Y", true)
                        )
                    }
                    .filter { it.name.isNotBlank() }
                    .toList()
            }
        } catch (e: Exception) {
            android.util.Log.e("ParkCSV", "Unable to load parks", e)
            emptyList()
        }
    }

    private fun parseCsvLine(line: String): List<String> {
        val fields = mutableListOf<String>()
        val current = StringBuilder()
        var inQuotes = false
        var i = 0

        while (i < line.length) {
            when (line[i]) {
                '"' -> {
                    if (inQuotes && i + 1 < line.length &&
                        line[i + 1] == '"') {
                        current.append('"')
                        i++
                    } else {
                        inQuotes = !inQuotes
                    }
                }
                ',' -> {
                    if (inQuotes) current.append(',')
                    else {
                        fields.add(current.toString())
                        current.clear()
                    }
                }
                else -> current.append(line[i])
            }
            i++
        }
        fields.add(current.toString())
        return fields
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val csvParks = loadParksFromCSV()
        setContent {
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

@Composable
fun Greeting(
    name: String,
    parks: List<Park>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
    var searchText by remember { mutableStateOf("") }

    var selectedAmenity by remember {
        mutableStateOf("All")
    }

    val filteredParks = parks.filter { park ->
        val matchesSearch = park.name.contains(
            searchText,
            ignoreCase = true
        )

        val matchesAmenity = when (selectedAmenity) {
            "Walking Trails" -> park.trails
            "Playgrounds" -> park.playground
            "Picnic Areas" -> park.picnicArea
            else -> true
        }

        matchesSearch && matchesAmenity
    }

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
            Text(text = park.name)
        }

        if (filteredParks.isEmpty()) {
            Text(text = "No parks found")
        }

    } // Closes Column
} // Closes Greeting




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CityParkTrailExplorerTheme {
        Greeting(
            name = "Android",
            parks = listOf(
                Park(
                    name = "Sample Park",
                    address = "Tallahassee, FL",
                    amenities = "Walking Trails",
                    trails = true,
                    playground = false,
                    picnicArea = false
                )
            ),
            modifier = Modifier
        )
    }
}