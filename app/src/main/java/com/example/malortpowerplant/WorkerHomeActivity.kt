package com.example.malortpowerplant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AlertDialog
import java.util.Timer
import kotlin.concurrent.schedule

class WorkerHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker_home)
        warningNotification()
    }
    private fun warningNotification(){
        Handler().postDelayed(
            {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("RADIATION WARNING")
                builder.setMessage("You need to leave the room in 10 minutes!")
                builder.setPositiveButton("OK", null)
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }, 5000)
    }
}