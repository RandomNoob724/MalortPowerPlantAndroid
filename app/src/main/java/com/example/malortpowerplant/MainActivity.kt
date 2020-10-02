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
        CloudFirestore.instance.updateClockedIn("361F1FF8")
        setContentView(R.layout.activity_start)

        //CloudFirestore.instance.updateClockedIn("361F1FF8")

        val uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
        val bluetoothChipID = "98:D3:11:F8:6B:81"
        var bluetoothSocket: BluetoothSocket? = null
        var connectButton = findViewById<Button>(R.id.connectButton)
        var text = findViewById<TextView>(R.id.connectionLabel)
        var loadingIndicator = findViewById<ProgressBar>(R.id.loadingIndicator)

        loadingIndicator.visibility = View.VISIBLE

        var payloadData = findViewById<EditText>(R.id.text_to_send)
        var sendButton = findViewById<Button>(R.id.send_button)

        sendButton.setOnClickListener{
            BluetoothHandler.bluetoothScope.launch {
                BluetoothHandler.sendData(payloadData.text.toString())
            }
        }

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
                connectionLabel.text = "Disconnected"
                loadingIndicator.visibility = View.GONE
            } else {
                Log.d("bluetooth", "Connecting Socket")
                BluetoothHandler.createBluetoothConnectionWithId(bluetoothChipID, uuid)
                connectionLabel.text = "Connected"
                loadingIndicator.visibility = View.GONE
            }
            if(BluetoothHandler.getConnection()){
                connectionLabel.text = "Connected"
                loadingIndicator.visibility = View.GONE
                BluetoothHandler.bluetoothScope.launch {
                    BluetoothHandler.awaitIncomingBluetoothData()
                }
                val intent = Intent(this, WorkerHomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                connectionLabel.text = "Error"
                loadingIndicator.visibility = View.GONE
            }
        }
    }
}