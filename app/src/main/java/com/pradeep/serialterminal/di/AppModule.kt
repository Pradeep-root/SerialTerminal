package com.pradeep.serialterminal.di

import android.content.Context
import com.pradeep.bluetooth.data.BluetoothTerminalController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBluetoothController(@ApplicationContext context: Context): BluetoothTerminalController {
        return BluetoothTerminalController(context = context)
    }

}