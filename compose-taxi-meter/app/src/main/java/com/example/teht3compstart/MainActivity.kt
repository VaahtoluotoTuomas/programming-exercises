package com.example.teht3compstart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teht3compstart.ui.theme.Teht3CompStartTheme
import androidx.core.content.edit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = getPreferences(MODE_PRIVATE)

        val defaultLahto = sharedPref.getFloat("lahtomaksu", 5.0f)
        val defaultKmTaksa = sharedPref.getFloat("kmtaksa", 1.5f)

        setContent {
            Teht3CompStartTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting(
                        aloitusLahtomaksu = defaultLahto,
                        aloitusKmTaksa = defaultKmTaksa,
                        sharedPref = sharedPref)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun Greeting(
    aloitusLahtomaksu: Float,
    aloitusKmTaksa: Float,
    sharedPref: android.content.SharedPreferences
) {
    val materialBlue700 = Color(0xFF1976D2)

    var expanded by remember { mutableStateOf(false) }
    var screen by remember { mutableIntStateOf(0) }   // 0 = pääsivu
    val localFocusManager = LocalFocusManager.current

    var matkanPituusInput by remember { mutableStateOf("") }
    var taksa by remember { mutableFloatStateOf(0.0f) }
    var virheIlmoitus by remember { mutableStateOf(false) }

    var lahtomaksu by remember { mutableFloatStateOf(aloitusLahtomaksu) }
    var kmTaksa by remember { mutableFloatStateOf(aloitusKmTaksa) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text("Taksamittari  ") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = materialBlue700),
                navigationIcon = {
                    IconButton(onClick = { if (screen == 1) screen = 0 }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Takaisin")
                    }
                },
                actions = {
                    // 3 vertical dots icon
                    IconButton(onClick = { expanded = true }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Asetukset")
                    }
                    // drop down menu
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(text = { Text("Asetukset") }, onClick = {
                            expanded = false
                            screen = 1
                        })
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            if (screen == 0) {
                FloatingActionButton(onClick = {
                    val pituus = matkanPituusInput.toFloatOrNull()
                    if (pituus != null && pituus >= 0) {
                        taksa = lahtomaksu + (pituus * kmTaksa)
                        virheIlmoitus = false
                        localFocusManager.clearFocus()
                    } else {
                        virheIlmoitus = true
                    }
                }) {
                    Text("+")
                }
            }
        },

        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .consumeWindowInsets(innerPadding)
                    .padding(innerPadding)
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            localFocusManager.clearFocus()
                        })
                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                when (screen) {
                    0 -> {
                        OutlinedTextField(
                            value = matkanPituusInput,
                            onValueChange = {
                                matkanPituusInput = it.replace(',', '.')
                                virheIlmoitus = false
                            },
                            label = { Text("Matkan pituus (km)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            isError = virheIlmoitus,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        if (virheIlmoitus) {
                            Text(text = "Syötä kelvollinen luku!", color = Color.Red, modifier = Modifier.padding(bottom = 8.dp))
                        }
                        val formatoituTaksa = String.format(java.util.Locale.US, "%.1f", taksa)
                        Text(
                            text = "Taksa = $formatoituTaksa €",
                            modifier = Modifier.padding(10.dp),
                            style = MaterialTheme.typography.headlineMedium
                            )
                    }

                    1 -> {

                        BackHandler { screen = 0 }

                        var lahtoInput by remember { mutableStateOf(lahtomaksu.toString()) }
                        var kmInput by remember { mutableStateOf(kmTaksa.toString()) }
                        var asetusVirhe by remember { mutableStateOf(false) }

                        OutlinedTextField(
                            value = lahtoInput,
                            onValueChange = {
                                lahtoInput = it.replace(',', '.')
                                asetusVirhe = false
                            },
                            label = { Text("Lähtömaksu (€)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            isError = asetusVirhe,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        OutlinedTextField(
                            value = kmInput,
                            onValueChange = {
                                kmInput = it.replace(',', '.')
                                asetusVirhe = false
                            },
                            label = { Text("Kilometritaksa (€/km)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            isError = asetusVirhe,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        Button(
                            onClick = {
                                val uusiLahto = lahtoInput.toFloatOrNull()
                                val uusiKm = kmInput.toFloatOrNull()

                                if (uusiLahto != null && uusiKm != null && uusiLahto >= 0 && uusiKm >= 0) {
                                    lahtomaksu = uusiLahto
                                    kmTaksa = uusiKm

                                    sharedPref.edit {
                                        putFloat("lahtomaksu", uusiLahto)
                                        putFloat("kmtaksa", uusiKm)
                                    }

                                    localFocusManager.clearFocus()
                                    screen = 0
                                } else {
                                    asetusVirhe = true
                                }
                            }
                        )
                        {
                            Text("Tallenna")
                        }
                        if (asetusVirhe) {
                            Text(text = "Tarkista syöttämäsi luvut!", color = Color.Red, modifier = Modifier.padding(top = 8.dp))
                        }
                    }
                }
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Teht3CompStartTheme {
        Greeting(5.0f, 1.5f, LocalContext.current.getSharedPreferences("test", 0))
    }
}
