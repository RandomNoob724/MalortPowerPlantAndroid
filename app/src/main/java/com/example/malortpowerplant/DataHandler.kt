package com.example.malortpowerplant

import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.UiThread
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.CoroutineContext

class CloudFirestore: CoroutineScope {
    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    companion object{
        val instance = CloudFirestore()
    }

    private val db = FirebaseFirestore.getInstance()

    fun updateClockedIn(id: String){
        val workerRef = db.collection("Worker").document(id)

        launch {
            val worker = checkIfClockedIn(id)
            val clockedIn = worker.data?.get("clockedIn") as Boolean

            if(clockedIn){
                workerRef.update(
                    "clockedIn", false
                )
            }
            else{
                workerRef.update(
                    "clockedIn", true
                )
            }
        }

    }

    private suspend fun checkIfClockedIn(id: String) =
        db.collection("Worker").document(id).get().await()
}