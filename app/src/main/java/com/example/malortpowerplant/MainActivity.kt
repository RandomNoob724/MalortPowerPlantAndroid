package com.example.malortpowerplant

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlinx.android.synthetic.main.activity_start.*
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        
        //CloudFirestore.instance.updateClockedIn("361F1FF8")

        var payloadData = findViewById<EditText>(R.id.text_to_send)
        var sendButton = findViewById<Button>(R.id.send_button)

        sendButton.setOnClickListener{
            BluetoothHandler.bluetoothScope.launch {
                BluetoothHandler.sendData(payloadData.text.toString())
            }
        }
    }
}