package com.example.malortpowerplant

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import java.io.BufferedInputStream
import java.io.DataInputStream
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.Method
import java.util.*
import com.example.malortpowerplant.BluetoothHandler
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bluetoothHandler: BluetoothHandler = BluetoothHandler(this)
        val uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
        val bluetoothChipID = "98:D3:11:F8:6B:81"
        var bluetoothSocket: BluetoothSocket? = null
        var connectButton = findViewById<Button>(R.id.connectButton)
        var text = findViewById<TextView>(R.id.fucking_text)

        connectButton.setOnClickListener{
            Log.d("bluetooth", bluetoothHandler.getConnection().toString())
            if(bluetoothHandler.getConnection()){
                Log.d("bluetooth", "Disconnecting Socket")
                bluetoothSocket!!.close()
                fucking_text.text = "Disconnected"
            } else {
                Log.d("bluetooth", "Connecting Socket")
                bluetoothSocket = bluetoothHandler.createBluetoothConnectionWithId(bluetoothChipID, uuid)
                fucking_text.text = "Connected"
            }
        }



        //bluetoothHandler.awaitIncomingBluetoothData(bluetoothSocket)

    }
}