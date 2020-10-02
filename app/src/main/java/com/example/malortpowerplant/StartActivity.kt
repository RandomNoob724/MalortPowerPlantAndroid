package com.example.malortpowerplant

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_start.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.*

class StartActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        var loadingIndicator = findViewById<ProgressBar>(R.id.loadingIndicator)
        var connectButton = findViewById<Button>(R.id.connectButton)

        val uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
        val bluetoothChipID = "98:D3:11:F8:6B:81"

        loadingIndicator.visibility = View.VISIBLE

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
                val intent = Intent(this, WorkerHomeActivity::class.java)
                startActivity(intent)
            } else {
                connectionLabel.text = "Error"
                loadingIndicator.visibility = View.GONE
            }
        }
    }
}