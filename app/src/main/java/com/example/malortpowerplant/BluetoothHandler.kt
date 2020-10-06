package com.example.malortpowerplant

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.util.Log
import com.google.firebase.FirebaseException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*
import kotlin.concurrent.timer


object BluetoothHandler : Thread(){
    val bluetoothScope = CoroutineScope(Dispatchers.IO)
    private var bluetoothSocket: BluetoothSocket? = null
    public var radiationOutput = 30

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
                            Log.d("bluetooth", "Direct value"+String(buffer))
                        }

                        if(String(buffer)[0] == 'P'){
                            var value: String
                            if(String(buffer).length == 3){
                                value = String(buffer)[1].toString()
                            } else if(String(buffer).length == 4) {
                                value = String(buffer)[1].toString() + String(buffer)[2].toString()
                            } else{
                                value = "100"
                            }
                            Log.d("bluetooth", value)
                            RadiationHandler.setRadiationOutput(value.toInt())

                            var radValue = RadiationHandler.calculateSafetyTime()
                            Log.d("bluetooth", radValue.toString())
                            RadiationHandler.setRadiationOutput(value.toInt())
                            sendData("Tj"+radValue.toString())
                        }
                        if(String(buffer)[0] == 'C'){
                            var rfid = String(buffer)
                            rfid = rfid.removeRange(0.rangeTo(0))
                            Log.d("bluetooth", rfid)
                            if(rfid != "C"){
                                try {
                                    CloudFirestore.instance.updateClockedIn(rfid)
                                    bluetoothScope.launch {
                                        var clockedIn = CloudFirestore.instance.checkIfClockedIn(rfid)
                                        var bitToSend: Boolean =  clockedIn.data?.get("clockedIn") as Boolean
                                        Log.d("bluetooth", bitToSend.toString())
                                        if(!bitToSend){
                                            Log.d("bluetooth", "C1"+rfid)
                                            sendData("C1"+rfid)
                                        } else {
                                            Log.d("bluetooth", "C0"+rfid)
                                            sendData("C0"+rfid)
                                            BluetoothHandler.bluetoothSocket!!.close()
                                        }
                                    }
                                }catch (e: FirebaseException){
                                    Log.d("firebase error", e.toString())
                                }
                            }
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