package com.example.pocbroadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony
import android.telephony.SmsMessage


@Suppress("UNCHECKED_CAST")
open class SMSBroadcast : BroadcastReceiver() {

    private var smsListener: SmsListener? = null
    private lateinit var smsMessage: SmsMessage

    override fun onReceive(context: Context, intent: Intent) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION == intent.action) {
            val data = intent.extras
            val pdus = data?.get("pdus") as Array<Any>

            for (position in pdus.indices) {
                smsMessage = verifyVersionForCreatePDU(intent, pdus, position)
                val sender = smsMessage.displayOriginatingAddress

                if (sender == NUMBER_ORIGIN) {
                    val messageBody = smsMessage.messageBody
                    smsListener?.messageReceived(messageBody)
                }
            }
        }
    }

    private fun verifyVersionForCreatePDU(
        intent: Intent,
        pdus: Array<Any>,
        position: Int
    ): SmsMessage {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val format = intent.getStringExtra("format")
            SmsMessage.createFromPdu(pdus[position] as ByteArray, format)
        } else {
            SmsMessage.createFromPdu(pdus[position] as ByteArray)
        }
    }

    open fun bindListener(smsListener: SmsListener) {
        this.smsListener = smsListener
    }

    companion object {
        private const val NUMBER_ORIGIN = "6505551212"
    }
}