package com.example.acquaint.xmppappdemo.app

import android.app.Application
import android.content.Intent

import com.example.acquaint.xmppappdemo.connectionService.XmppConnection
import com.example.acquaint.xmppappdemo.connectionService.XmppConnectionService

class XmppApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if (XmppConnectionService.state == XmppConnection.ConnectionState.DISCONNECTED) {
            val i1 = Intent(this, XmppConnectionService::class.java)
            startService(i1)
        }

    }

    companion object {

        var loginUserName = ""
    }
}
