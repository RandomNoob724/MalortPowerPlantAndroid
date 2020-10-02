package com.example.malortpowerplant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.launch
import android.util.Log
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_worker_home.*
import java.util.Timer
import kotlin.concurrent.schedule
import kotlin.concurrent.timer

class WorkerHomeActivity : AppCompatActivity() {

    private lateinit var countDownTimer: CountDownTimer
    private var radiationOutput = BluetoothHandler.radiationOutput

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker_home)
        //var debugButton = findViewById<Button>(R.id.debug)

        BluetoothHandler.bluetoothScope.launch {
            BluetoothHandler.awaitIncomingBluetoothData()
        }

        //debugButton.setOnClickListener{
        //    val intent = Intent(this, MainActivity::class.java)
        //    startActivity(intent)
        //}
        val timerLabel = findViewById<TextView>(R.id.timerLabel)
        val timer = RadiationHandler.setRadiationTimer()
        timer.start()
        updateTimerUI()
    }

    private fun warningNotification(hours: Int, minutes: Int, seconds: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("RADIATION WARNING")
        builder.setMessage("You need to leave the room in $hours hours, $minutes minutes and $seconds seconds!")
        builder.setPositiveButton("OK", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun updateTimerUI() {
        countDownTimer =
            object : CountDownTimer(1000000000000000L, 1000) {
                override fun onFinish() {
                    Log.d("u ded", "u ded")
                }

                override fun onTick(millisUntilFinished: Long) {
                    val hours = RadiationHandler.getHours()
                    val minutes = RadiationHandler.getMinutes()
                    val seconds = RadiationHandler.getSeconds()
                    var stringHours = ""
                    var stringMinutes = ""
                    var stringSeconds = ""
                    if(hours < 10) {
                        stringHours = "0$hours"
                    } else {
                        stringHours = "$hours"
                    }
                    if(minutes < 10) {
                        stringMinutes = "0$minutes"
                    } else {
                        stringMinutes = "$minutes"
                    }
                    if(seconds < 10) {
                        stringSeconds = "0$seconds"
                    } else {
                        stringSeconds = "$seconds"
                    }

                    timerLabel.text = stringHours + ":" + stringMinutes + ":" + stringSeconds

                    if (hours == 2 && minutes == 53 && seconds == 30) {
                        warningNotification(hours, minutes, seconds)
                    }
                }
            }
        countDownTimer.start()
    }
}