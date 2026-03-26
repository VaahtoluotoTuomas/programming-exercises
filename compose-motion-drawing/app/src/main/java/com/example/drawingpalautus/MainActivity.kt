package com.example.drawingpalautus

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drawingpalautus.ui.theme.DrawingPalautusTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            DrawingPalautusTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PiirtoScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun PiirtoScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }

    val reitti = remember { mutableStateListOf<Offset>() }
    val sensorihistoria = remember { mutableStateListOf<String>() }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val leveysPx = with(LocalDensity.current) { maxWidth.toPx() }
        val korkeusPx = with(LocalDensity.current) { maxHeight.toPx() }

        var palloX by remember { mutableFloatStateOf(leveysPx / 2) }
        var palloY by remember { mutableFloatStateOf(korkeusPx / 2) }

        DisposableEffect(Unit) {
            val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

            val listener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    event?.let {
                        val x = it.values[0]
                        val y = it.values[1]
                        //val z = it.values[2]

                        val nopeusKerroin = 1.5f

                        palloX = (palloX - x * nopeusKerroin).coerceIn(20f, leveysPx - 20f)
                        palloY = (palloY + y * nopeusKerroin).coerceIn(20f, korkeusPx - 20f)

                        reitti.add(Offset(palloX, palloY))

                        sensorihistoria.add("X: ${"%.1f".format(x)} | Y: ${"%.1f".format(y)}")

                        if (sensorihistoria.size > 10) {
                            sensorihistoria.removeAt(0)
                        }

                        if (reitti.size > 1500) {
                            reitti.removeAt(0)
                        }
                    }
                }
                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
            }

            sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_UI)

            onDispose {
                sensorManager.unregisterListener(listener)
            }
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            reitti.forEach { sijainti ->
                drawCircle(
                    color = Color.Blue,
                    radius = 20f,
                    center = sijainti
                )
            }

            if (reitti.isNotEmpty()) {
                drawCircle(
                    color = Color.Red,
                    radius = 25f,
                    center = reitti.last()
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .background(Color.White.copy(alpha = 0.7f))
                .padding(8.dp)
        ) {
            Text(
                text = "Sensorihistoria:",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
            sensorihistoria.forEach { arvo ->
                Text(text = arvo, fontSize = 12.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PiirtoScreenPreview() {
    DrawingPalautusTheme {
        PiirtoScreen()
    }
}