package com.example.painoindeksicompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.painoindeksicompose.ui.theme.PainoIndeksiComposeTheme
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PainoIndeksiComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        Laskuri()
                    }
                }
            }
        }
    }
}

@Composable
fun Laskuri() {
    var pituus by remember { mutableStateOf("") }
    var paino by remember { mutableStateOf("")}
    var onkoAikuinen by remember { mutableStateOf(false)}
    var tulos by remember { mutableStateOf("")}

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {

        OutlinedTextField(
            value = pituus,
            onValueChange = { pituus = it},
            label = { Text("Pituus (cm)")},
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = paino,
            onValueChange = { paino = it},
            label = { Text("Paino (kg)")},
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (tulos.isNotEmpty()) {
            Text(
                text = tulos,
                textAlign = TextAlign.Center
            )
        }
        else {
            Text(
                text = "Painoindeksi",
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = onkoAikuinen,
                onCheckedChange = { onkoAikuinen = it }
            )
            Text(text = "Olen aikuinen")
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                val pituusArvo = pituus.replace(',', '.').toDoubleOrNull() ?: 0.0
                val painoArvo = paino.replace(',', '.').toDoubleOrNull() ?: 0.0

                if (pituusArvo > 0 && painoArvo > 0 && onkoAikuinen) {
                    val pituusMetreina = pituusArvo / 100
                    val laskettuArvo = 1.3 * painoArvo / pituusMetreina.pow(2.5)
                    tulos = String.format("Painoindeksi: %.2f", laskettuArvo)
                }
                else if (!onkoAikuinen) {
                    tulos = "Sinun täytyy olla aikuinen, jotta voit laskea painoindeksin"
                }
                else {
                    tulos = "Painoindeksiä ei voitu laskea"
                }
            }
        ) {
            Text("Laske")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LaskuriPreview() {
    PainoIndeksiComposeTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Laskuri()
            }
        }
    }
}