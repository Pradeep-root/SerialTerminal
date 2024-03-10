package com.pradeep.data

import android.app.Activity
import android.bluetooth.BluetoothDevice

interface BluetoothTerminal {

    fun isBluetoothSupported(): Boolean

    fun isBluetoothEnabled(): Boolean

    fun enableBluetooth()

    fun startDiscovery()

    fun getAllPairedDevices(discoveryCallback: (BluetoothDevice) -> Unit)

}