package com.pradeep.bluetooth.mapper

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import com.pradeep.bluetooth.model.BluetoothDeviceDomain

@SuppressLint("MissingPermission")
fun BluetoothDevice.toBluetoothDeviceDomain(): BluetoothDeviceDomain {
    return BluetoothDeviceDomain(
        name = name,
        address = address
    )
}