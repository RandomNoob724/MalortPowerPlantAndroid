package com.example.malortpowerplant

import android.R
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.Settings.Global.DEVICE_NAME
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.*
import java.util.*
import kotlinx.coroutines.*

object BluetoothHandler : Thread(){
    val bluetoothScope = CoroutineScope(Dispatchers.IO)
    private var bluetoothSocket: BluetoothSocket? = null

    fun createBluetoothConnectionWithId(id: String, uuid: UUID) {
        var bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        var bluetoothDevice: BluetoothDevice? = null

        try {
            if(bluetoothAdapter != null){
                if(bluetoothAdapter.isEnabled){
                    bluetoothDevice = bluetoothAdapter.getRemoteDevice(id)
                    this.bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid)
                    if(bluetoothSocket != null){
                        bluetoothSocket?.connect()
                    }
                    Log.d("bluetooth", "Bluetooth is now connected " + bluetoothSocket?.isConnected.toString())
                }
            }
        } catch(e: IOException){
            Log.d("bluetooth", e.toString())
        }
    }

    suspend fun awaitIncomingBluetoothData(){
        var inputStream: DataInputStream?
        var bufferedOutputStream: BufferedOutputStream?
        var rfid: String = ""
        val bytes: Int
        Log.d("bluetooth", (this.bluetoothSocket != null).toString())
        if(this.bluetoothSocket != null){
            inputStream = DataInputStream(this.bluetoothSocket?.inputStream)
            bytes = inputStream.read()
            Log.d("bluetooth",  bytes.toString())
        } else {
            Log.d("bluetooth", "The bluetoothsocket is now null")
        }
    }

    fun checkBluetoothAdapter(): Boolean{
        var bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        return bluetoothAdapter?.isEnabled ?: false
    }

    fun getBluetoothSocket(): BluetoothSocket? {
        if(this.bluetoothSocket == null){
            return null
        } else {
            return bluetoothSocket as BluetoothSocket
        }
    }

    fun getConnection(): Boolean {
        if(this.bluetoothSocket != null){
            return this.bluetoothSocket!!.isConnected
        } else {
            return false
        }
    }
}