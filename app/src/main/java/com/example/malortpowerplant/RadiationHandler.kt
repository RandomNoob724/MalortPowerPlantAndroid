package com.example.malortpowerplant

import android.os.CountDownTimer
import android.util.Log

class RadiationHandler{
    companion object{
        val instance = RadiationHandler()
    }

    private val roomCoefficient = 1.6
    private val protectiveCoefficient = 1
    private var currentRadiation = 0.0
    private val radiationSafety = 500000
    private var radiationOutput = 30
    private lateinit var countDownTimer: CountDownTimer
    private var timeRemaining = 0L
    private var radsPerSecond: Double = 30.0

    private var hours = 0
    private var minutes = 0
    private var seconds = 0

    fun calculateSafetyTime(): Long{
        radsPerSecond = (radiationOutput*roomCoefficient)/protectiveCoefficient
        return ((radiationSafety - currentRadiation)/radsPerSecond).toLong()
    }

    fun setRadiationTimer(): CountDownTimer{
        countDownTimer = object: CountDownTimer(calculateSafetyTime()*1000L, 1000){
            override fun onFinish() {
                Log.d("u ded", "u ded")
            }

            override fun onTick(millisUntilFinished: Long) {
                timeRemaining = millisUntilFinished / 1000
                timeFormation(timeRemaining)
                currentRadiation += radsPerSecond
            }
        }
        return countDownTimer
    }

    fun timeFormation(timeRemaining: Long){
        seconds = (timeRemaining % 60).toInt()
        minutes = ((timeRemaining/60) % 60).toInt()
        hours = (timeRemaining/3600).toInt()
    }

    fun setRadiationOutput(newRadiationOutput: Int){
        radiationOutput = newRadiationOutput

    }

    fun getSeconds(): Int{
        return seconds
    }

    fun getMinutes(): Int{
        return minutes
    }

    fun getHours(): Int{
        return hours
    }
}