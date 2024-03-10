package com.pradeep.data

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Parcelable
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat


class BluetoothTerminalImp(private val context: Context) : BluetoothTerminal {

    private val activity = context as ComponentActivity
    private var bluetoothCallback: BluetoothCallback? = null

    companion object {
        private const val TAG = "BluetoothTerminal"
        private const val DISCOVERABLE_DURATION = 300
    }

    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private val receiver = object : BroadcastReceiver() {
        var device: Array<out Parcelable>? = null
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    device = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intent.getParcelableArrayExtra(
                            BluetoothDevice.EXTRA_DEVICE,
                            BluetoothDevice::class.java
                        )
                    } else {
                        intent.getParcelableArrayExtra(
                            BluetoothDevice.EXTRA_DEVICE
                        )
                    }
                    if (bluetoothCallback != null) {
                        device?.let { bluetoothCallback?.onDeviceFound(it) }
                    } else {
                        Log.d(
                            TAG,
                            "BluetoothCallback is not registered, register using onDeviceFound()"
                        )
                    }
                }
            }
        }
    }

    override fun isBluetoothSupported(): Boolean {
        return bluetoothAdapter != null
    }

    override fun isBluetoothEnabled(): Boolean {
        return bluetoothAdapter?.isEnabled ?: false
    }

    override fun enableBluetooth() {
        val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        val enableBluetooth =
            activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}
        if (isBluetoothSupported() && !isBluetoothEnabled()) {
            enableBluetooth.launch(enableBluetoothIntent)
        }
    }

    override fun startDiscovery() {
        val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        discoverableIntent.putExtra(
            BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
            DISCOVERABLE_DURATION
        )
        val requestDiscoverable =
            activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == DISCOVERABLE_DURATION) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        checkBluetoothPermission(
                            activity = activity,
                            context = context,
                            checkPermissionId = Manifest.permission.BLUETOOTH_SCAN,
                            requestPermissionId = Manifest.permission.BLUETOOTH_SCAN,
                            onRequestPermissionsResult = {
                                if (bluetoothAdapter?.isDiscovering == false) {
                                    bluetoothAdapter?.startDiscovery()
                                }
                            })
                    } else {
                        if (bluetoothAdapter?.isDiscovering == false) {
                            bluetoothAdapter?.startDiscovery()
                        }
                    }
                } else {
                    Log.d(TAG, "Request Discoverable got fail")
                }
            }
        requestDiscoverable.launch(discoverableIntent)

    }


    // TODO Caution: Performing device discovery consumes a lot of the Bluetooth adapter's resources.
    //  After you have found a device to connect to, be certain that you stop discovery with
    //  cancelDiscovery() before attempting a connection. Also, you shouldn't
    //  perform discovery while connected to a device because the discovery
    //  process significantly reduces the bandwidth available for any existing connections.

    override fun getAllPairedDevices(discoveryCallback: (BluetoothDevice) -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            checkBluetoothPermission(
                activity = activity,
                context = context,
                checkPermissionId = Manifest.permission.BLUETOOTH_CONNECT,
                requestPermissionId = Manifest.permission.BLUETOOTH_CONNECT,
                onRequestPermissionsResult = {
                    val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
                    pairedDevices?.forEach(discoveryCallback)
                })
        } else {
            val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
            pairedDevices?.forEach(discoveryCallback)
        }
    }

    fun registerCallback(callback: BluetoothCallback) {
        bluetoothCallback = callback
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        activity.registerReceiver(receiver, filter)
    }

    fun unRegisterCallback() {
        bluetoothCallback = null
    }

    private fun checkBluetoothPermission(
        activity: ComponentActivity,
        context: Context,
        checkPermissionId: String,
        requestPermissionId: String,
        onRequestPermissionsResult: (Boolean) -> Unit
    ) {
        @RequiresApi(Build.VERSION_CODES.S)
        if (ContextCompat.checkSelfPermission(
                context,
                checkPermissionId
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted, so request it
            val requestPermissionLauncher =
                activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                    onRequestPermissionsResult(isGranted)
                }
            requestPermissionLauncher.launch(requestPermissionId)
        } else {
            onRequestPermissionsResult(true)
        }
    }

}