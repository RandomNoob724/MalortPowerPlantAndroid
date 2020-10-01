package com.example.malortpowerplant

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //CloudFirestore.instance.updateClockedIn("361F1FF8")
        val timer = RadiationHandler.instance.setRadiationTimer()
        timer.start()

        val uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
        val bluetoothChipID = "98:D3:11:F8:6B:81"
        var bluetoothSocket: BluetoothSocket? = null
        var connectButton = findViewById<Button>(R.id.connectButton)
        var text = findViewById<TextView>(R.id.fucking_text)

        if(!BluetoothHandler.checkBluetoothAdapter()){
            val intent: Intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(intent,1)
        }

        connectButton.setOnClickListener{
            Log.d("bluetooth", BluetoothHandler.getConnection().toString())
            if(BluetoothHandler.getConnection()){
                Log.d("bluetooth", "Disconnecting Socket")
                BluetoothHandler.getBluetoothSocket()!!.close()
                BluetoothHandler.bluetoothScope.cancel()
                fucking_text.text = "Disconnected"
            } else {
                Log.d("bluetooth", "Connecting Socket")
                BluetoothHandler.createBluetoothConnectionWithId(bluetoothChipID, uuid)
                fucking_text.text = "Connected"
            }
            if(BluetoothHandler.getConnection()){
                fucking_text.text = "Connected"
                BluetoothHandler.bluetoothScope.launch {
                    BluetoothHandler.awaitIncomingBluetoothData()
                }
            } else {
                fucking_text.text = "Error"
            }
        }
    }
}