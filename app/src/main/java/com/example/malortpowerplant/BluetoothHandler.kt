package com.example.malortpowerplant

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.system.Os.socket
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.io.*
import java.util.*


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
        Log.d("bluetooth", (this.bluetoothSocket != null).toString())
        if(this.bluetoothSocket != null){

            var bytes = "" // bytes returned from read()

            try {
                var tmpIn: BufferedInputStream? = null

                // Get the BluetoothSocket input stream
                tmpIn = BufferedInputStream(this.bluetoothSocket!!.inputStream)
                val mmInStream = DataInputStream(tmpIn)

                // Read from the InputStream
                while(tmpIn.read().toChar() != '/'){
                    bytes += tmpIn.read().toChar()
                    Log.d("bluetooth", bytes)
                }
                // Send the obtained bytes to the UI Activity
            } catch (e: Exception) {
                Log.e("bluetooth", "Error with recieving message", e)
            }
        } else {
            Log.d("bluetooth", "The bluetoothsocket is now null")
        }
    }

    suspend fun sendData(payload: String){
        var byteArray: ByteArray = payload.toByteArray()
        try {
            bluetoothSocket!!.outputStream.write(byteArray)
            Log.d("bluetooth", "message sent through bluetooth")
        } catch (e: IOException){
            Log.d("bluetooth",  e.toString())
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