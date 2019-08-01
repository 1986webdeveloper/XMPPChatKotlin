package com.example.acquaint.xmppappdemo.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.EditText

import com.example.acquaint.xmppappdemo.R
import com.example.acquaint.xmppappdemo.connectionService.XmppConnection
import com.example.acquaint.xmppappdemo.connectionService.XmppConnectionService
import com.example.acquaint.xmppappdemo.model.RegisterRequestResponse
import com.example.acquaint.xmppappdemo.networking_setup.ApiClass
import com.example.acquaint.xmppappdemo.networking_setup.ApiInterface

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    lateinit var etUserName: EditText
    lateinit var etPassword: EditText
    lateinit var etFullName: EditText
    lateinit var etEmailAddress: EditText
    private val mOnRegisterClickListener = View.OnClickListener {
        if (TextUtils.isEmpty(etUserName.text.toString())) {
            etUserName.error = "Enter username"
            return@OnClickListener
        }
        if (TextUtils.isEmpty(etPassword.text.toString())) {
            etUserName.error = "Enter password"
            return@OnClickListener
        }
        if (TextUtils.isEmpty(etFullName.text.toString())) {
            etUserName.error = "Enter full name"
            return@OnClickListener
        }
        if (TextUtils.isEmpty(etEmailAddress.text.toString())) {
            etUserName.error = "Enter email address"
            return@OnClickListener
        }

        if (XmppConnectionService.state == XmppConnection.ConnectionState.DISCONNECTED) {
            val i1 = Intent(this@RegisterActivity, XmppConnectionService::class.java)
            startService(i1)
        }
        val apiInterface = ApiClass.retrofitObject
        val registerRequestResponse = RegisterRequestResponse()
        registerRequestResponse.username = etUserName.text.toString()
        registerRequestResponse.password = etPassword.text.toString()
        registerRequestResponse.name = etFullName.text.toString()
        registerRequestResponse.email = etEmailAddress.text.toString()
        val call = apiInterface.registerUser(registerRequestResponse)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 201) {
                    startActivity(Intent(this@RegisterActivity, Login::class.java))
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                call.cancel()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etUserName = findViewById(R.id.et_username)
        etPassword = findViewById(R.id.et_password)
        etFullName = findViewById(R.id.et_full_name)
        etEmailAddress = findViewById(R.id.et_email_address)
        findViewById<View>(R.id.btn_register).setOnClickListener(mOnRegisterClickListener)
    }
}
