package com.pradeep.serialterminal.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pradeep.bluetooth.model.BluetoothDevice
import com.pradeep.serialterminal.ui.BluetoothUiState
import com.pradeep.serialterminal.ui.theme.Blue_dark_background
import com.pradeep.serialterminal.ui.theme.Blue_light_background

@Composable
fun DevicesListScreen(
    state: BluetoothUiState,
    onStartScan: () -> Unit,
    onStopScan: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Blue_light_background)
            .padding(16.dp)
    ) {

        BluetoothDeviceList(
            pairedDevice = state.pairedDevices,
            scannedDevices = state.scannedDevices,
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                onClick = onStartScan,
                colors = ButtonDefaults.buttonColors(containerColor = Blue_dark_background)
            ) {
                Text(text = "Start scan")
            }
            Button(
                onClick = onStopScan,
                colors = ButtonDefaults.buttonColors(containerColor = Blue_dark_background)
            ) {
                Text(text = "Stop scan")
            }
        }
    }
}

@Composable
fun BluetoothDeviceList(
    pairedDevice: List<BluetoothDevice>,
    scannedDevices: List<BluetoothDevice>,
    onClick: (BluetoothDevice) -> Unit,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        modifier = modifier
    ) {
        item {
            Text(
                text = "Paired Devices",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
        items(pairedDevice) { device ->
            Text(
                text = device.name ?: "(No name)",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick(device) }
                    .padding(16.dp)
            )
        }

        item {
            Text(
                text = "Scanned Devices",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
        items(scannedDevices) { device ->
            Text(
                text = device.name ?: "(No name)",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick(device) }
                    .padding(16.dp)
            )
        }
    }
}

@Preview
@Composable
fun ScanDevicesListScreenPreview() {
    DevicesListScreen(state = BluetoothUiState(), {}, {})
}