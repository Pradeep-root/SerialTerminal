package com.pradeep.serialterminal

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.terminal.TerminalScreen
import com.pradeep.data.BluetoothCallback
import com.pradeep.data.BluetoothTerminalImp
import com.pradeep.serialterminal.ui.theme.SerialTerminalTheme

class MainActivity : ComponentActivity(), BluetoothCallback {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bluetoothTerminal = BluetoothTerminalImp(this)

        Log.d("bluetoothTerminal: ", "${bluetoothTerminal.isBluetoothSupported()}")
        Log.d("bluetoothTerminal: ", "${bluetoothTerminal.isBluetoothEnabled()}")
        bluetoothTerminal.enableBluetooth()
        Log.d("bluetoothTerminal: ", "${bluetoothTerminal.startDiscovery()}")

        bluetoothTerminal.getAllPairedDevices {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    Log.d("bluetoothTerminal: ", it.name)
                }
            } else {
                Log.d("bluetoothTerminal: ", it.name)
            }
        }

        bluetoothTerminal.registerCallback(this)


        setContent {
            SerialTerminalTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TerminalScreen()
                }
            }
        }
    }

    override fun onDeviceFound(device: Array<out Parcelable>) {
        Log.d("bluetoothTerminal:", device[0].toString())
    }
}
