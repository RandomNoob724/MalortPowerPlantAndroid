package com.example.malortpowerplant

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import java.io.BufferedInputStream
import java.io.DataInputStream
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.Method
import java.util.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
        var bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        val bluetoothChipID = "98:D3:11:F8:6B:81"
        var bluetoothSocket: BluetoothSocket? = null
        var bluetoothDevice: BluetoothDevice? = null
        val bufferedInputStream: BufferedInputStream
        var rfid: String = ""

        try {
            if(bluetoothAdapter != null){
                if(bluetoothAdapter.isEnabled){
                    bluetoothDevice = bluetoothAdapter.getRemoteDevice(bluetoothChipID)
                    bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid)
                    bluetoothSocket.connect()
                    Log.d("bluetooth", bluetoothSocket.isConnected.toString())
                }
            }
        } catch (e: IOException){
            Log.d("bluetooth", e.toString())
        }

        bufferedInputStream = BufferedInputStream(bluetoothSocket?.inputStream, 16)
        for (value in bufferedInputStream){
            if(rfid.length >= 8){
                rfid = ""
            }
            rfid += value.toChar()
            Log.d("bluetooth", rfid)
        }



        val fuckingText = findViewById<TextView>(R.id.fucking_text)
        fuckingText.text = bluetoothDevice?.name.toString()

    }
}