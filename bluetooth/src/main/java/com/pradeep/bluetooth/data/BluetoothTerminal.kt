package com.pradeep.bluetooth.data

import com.pradeep.bluetooth.model.BluetoothDeviceDomain
import kotlinx.coroutines.flow.StateFlow

interface BluetoothTerminal {

    val scannedDevices: StateFlow<List<BluetoothDeviceDomain>>
    val pairedDevices: StateFlow<List<BluetoothDeviceDomain>>

    fun startDiscovery()
    fun stopDiscovery()
    fun release()

}