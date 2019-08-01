package com.example.acquaint.xmppappdemo.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.EditText

import com.example.acquaint.xmppappdemo.R
import com.example.acquaint.xmppappdemo.app.XmppApp
import com.example.acquaint.xmppappdemo.connectionService.XmppConnection
import com.example.acquaint.xmppappdemo.connectionService.XmppConnectionService

class Login : AppCompatActivity() {

    private var etUsername: EditText? = null
    private var etPassword: EditText? = null
    private val mOnButtonClickListener = View.OnClickListener {
        if (TextUtils.isEmpty(etUsername!!.text.toString().trim { it <= ' ' })) {
            etUsername!!.error = "Username cannot be empty"
            etUsername!!.requestFocus()
            return@OnClickListener
        }
        if (TextUtils.isEmpty(etPassword!!.text.toString().trim { it <= ' ' })) {
            etPassword!!.error = "Password cannot be empty"
            etPassword!!.requestFocus()
            return@OnClickListener
        }
        XmppConnection.login(etUsername!!.text.toString(), etPassword!!.text.toString())
    }
    private val mOnLoginSuccessBroadcastReceived = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action!!.equals(XmppConnectionService.UI_AUTHENTICATED, ignoreCase = true)) {
                XmppApp.loginUserName = etUsername!!.text.toString().trim { it <= ' ' }
                ShowContactListActivity()
            }
        }
    }
    private val mOnRegisterButtonClickListener = View.OnClickListener {
        startActivity(Intent(this@Login, RegisterActivity::class.java))
        finish()
    }

    private fun ShowContactListActivity() {
        startActivity(Intent(this@Login, ContactListActivity::class.java))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.registerReceiver(mOnLoginSuccessBroadcastReceived, IntentFilter(XmppConnectionService.UI_AUTHENTICATED))
        etUsername = findViewById(R.id.et_username)
        etPassword = findViewById(R.id.et_password)
        findViewById<View>(R.id.btn_login).setOnClickListener(mOnButtonClickListener)
        findViewById<View>(R.id.btn_register).setOnClickListener(mOnRegisterButtonClickListener)
    }

    override fun onPause() {
        this.unregisterReceiver(mOnLoginSuccessBroadcastReceived)
        super.onPause()
    }
}
