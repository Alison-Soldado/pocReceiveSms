package com.example.pocbroadcast

interface SmsListener {
    fun messageReceived(message : String)
}