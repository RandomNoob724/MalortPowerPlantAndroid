package com.example.malortpowerplant

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedInputStream
import java.io.IOException
import java.util.*

class BluetoothHandler(activity: MainActivity): AppCompatActivity() {
    private var bluetoothSocket: BluetoothSocket? = null
    public fun createBluetoothConnectionWithId(id: String, uuid: UUID) : BluetoothSocket {
        var bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        var bluetoothDevice: BluetoothDevice? = null

        try {
            if(bluetoothAdapter != null){
                if(!bluetoothAdapter.isEnabled){
                    val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    startActivityForResult(enableBtIntent, 1)
                }
                else if(bluetoothAdapter.isEnabled){
                    bluetoothDevice = bluetoothAdapter.getRemoteDevice(id)
                    bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid)
                    bluetoothSocket!!.connect()
                    Log.d("bluetooth", "Bluetooth is now connected " + bluetoothSocket!!.isConnected.toString())
                }
            }
        } catch(e: IOException){
            Log.d("bluetooth", e.toString())
        }
        return bluetoothSocket!!
    }

    fun awaitIncomingBluetoothData(bluetoothSocket: BluetoothSocket){
        var bufferedInputStream: BufferedInputStream?
        var rfid: String = ""
        bufferedInputStream = BufferedInputStream(bluetoothSocket?.inputStream, 16)
        for (value in bufferedInputStream){
            if(rfid.length >= 8){
                rfid = ""
            }
            rfid += value.toChar()
            Log.d("bluetooth", rfid)
        }
    }

    fun getBluetoothSocket(): BluetoothSocket{
        return bluetoothSocket!!
    }

    fun getConnection(): Boolean {
        if(bluetoothSocket != null){
            return bluetoothSocket!!.isConnected
        } else {
            return false
        }
    }
}