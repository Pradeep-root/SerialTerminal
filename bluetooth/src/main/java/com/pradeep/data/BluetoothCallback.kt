package com.pradeep.data

import android.os.Parcelable

interface BluetoothCallback {

    fun onDeviceFound(device: Array<out Parcelable>)
}