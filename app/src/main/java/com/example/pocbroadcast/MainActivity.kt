package com.example.pocbroadcast

import android.Manifest
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Telephony
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SmsListener {

    private val smsBroadcast = SMSBroadcast()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun messageReceived(message: String) {
        activity_main_text_sms.text = message
    }

    override fun onResume() {
        super.onResume()
        checkPermissionSms()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(smsBroadcast)
    }

    private fun checkPermissionSms() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.RECEIVE_SMS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            enableListenerAndRegister()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.RECEIVE_SMS),
                NUMBER_PERMISSION_SMS
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == NUMBER_PERMISSION_SMS) {
            if (permissions[0] == Manifest.permission.SEND_SMS && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableListenerAndRegister()
            }
        }
    }

    private fun enableListenerAndRegister() {
        smsBroadcast.bindListener(this)
        registerReceiver(smsBroadcast, IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION))
    }

    companion object {
        private const val NUMBER_PERMISSION_SMS = 0
    }
}
