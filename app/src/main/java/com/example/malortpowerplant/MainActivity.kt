package com.example.malortpowerplant

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bluetoothChipUUID = "98:D3:11:F8:6B:81"
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        if(bluetoothAdapter == null){
            Log.d("bluetoothadapter", bluetoothAdapter.toString())
        } else {
            Log.d("bluetoothadapter", bluetoothAdapter.toString())
            if(!bluetoothAdapter.isEnabled){
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, 1)
            }
        }

        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        pairedDevices?.forEach{ device ->
            val deviceName = device.name
            val deviceAddress = device.address
            Log.d("bluetooth", deviceName.toString() + " " + deviceAddress.toString())
        }

        
        setContentView(R.layout.activity_main)
    }
}