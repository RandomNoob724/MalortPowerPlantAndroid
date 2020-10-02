package com.example.malortpowerplant

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*
import kotlin.concurrent.timer


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
            var bytes: Int // bytes returned from read()

            var availableBytes = 0
            // Keep listening to the InputStream until an exception occurs
            // Keep listening to the InputStream until an exception occurs
            while(bluetoothSocket!!.isConnected){
                try {
                    availableBytes = bluetoothSocket!!.inputStream.available()

                    if (availableBytes > 0) {
                        val buffer =
                            ByteArray(availableBytes) // buffer store for the stream
                        // Read from the InputStream
                        bytes = bluetoothSocket!!.inputStream.read(buffer)
                        Log.d("mmInStream.read(buffer);", String(buffer))
                        if (bytes > 0) {
                            // Send the obtained bytes to the UI activity
                            Log.d("bluetooth", String(buffer))
                        }

                        if(String(buffer)[0] == 'P'){
                            var value: String
                            if(String(buffer).length == 3){
                                value = String(buffer)[1].toString()
                            } else if(String(buffer).length == 4) {
                                value = String(buffer)[1].toString() + String(buffer)[2].toString()
                            } else {
                                value = "100"
                            }
                            Log.d("bluetooth", value)
                            RadiationHandler.instance.setRadiationOutput(value.toInt())
                            var radValue = RadiationHandler.instance.calculateSafetyTime()
                            Log.d("bluetooth", radValue.toString())
                            sendData("Tj"+radValue.toString())
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
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