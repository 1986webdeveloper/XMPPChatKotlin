package com.example.acquaint.xmppappdemo.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Toast

import com.example.acquaint.xmppappdemo.R
import com.example.acquaint.xmppappdemo.connectionService.XmppConnection
import com.example.acquaint.xmppappdemo.connectionService.XmppConnectionService

import org.jivesoftware.smack.chat2.Chat
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.chat2.IncomingChatMessageListener
import org.jivesoftware.smack.packet.Message
import org.jxmpp.jid.EntityBareJid

import co.intentservice.chatui.ChatView
import co.intentservice.chatui.models.ChatMessage


class ChatActivity : AppCompatActivity() {

    private var contactJid: String? = null
    private var mChatView: ChatView? = null
    private var mBroadcastReceiver: BroadcastReceiver? = null
    private var chatManager: ChatManager? = null
    private val OnIncomingMessageListener = IncomingChatMessageListener { from, message, chat ->
        Log.e(TAG, "newIncomingMessage: " + message.body)
        //if (from.equals(contactJid)) {
        runOnUiThread {
            val chatMessage = ChatMessage(message.body, System.currentTimeMillis(), ChatMessage.Type.RECEIVED)
            mChatView!!.addMessage(chatMessage)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        mChatView = findViewById(R.id.rooster_chat_view)

        mChatView!!.setOnSentMessageListener {
            if (XmppConnectionService.state == XmppConnection.ConnectionState.CONNECTED) {
                Log.d(TAG, "The client is connected to the server,Sending Message")
                val intent = Intent(XmppConnectionService.SEND_MESSAGE)
                intent.putExtra(XmppConnectionService.BUNDLE_MESSAGE_BODY,
                        mChatView!!.typedMessage)
                intent.putExtra(XmppConnectionService.BUNDLE_TO, contactJid)
                sendBroadcast(intent)
            } else {
                Toast.makeText(applicationContext,
                        "Client not connected to server ,Message not sent!",
                        Toast.LENGTH_LONG).show()
            }
            true
        }

        val intent = intent
        contactJid = intent.getStringExtra("EXTRA_CONTACT_JID")
        title = contactJid
        //getUserDetails(contactJid);

        chatManager = ChatManager.getInstanceFor(XmppConnection.instance)
        chatManager!!.addIncomingListener(OnIncomingMessageListener)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(mBroadcastReceiver)
    }

    override fun onResume() {
        super.onResume()
        mBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val action = intent.action
                when (action) {
                    XmppConnectionService.NEW_MESSAGE -> {
                        val from = intent.getStringExtra(XmppConnectionService.BUNDLE_FROM_JID)
                        val body = intent.getStringExtra(XmppConnectionService.BUNDLE_MESSAGE_BODY)
                        if (from == contactJid) {
                            val chatMessage = ChatMessage(body, System.currentTimeMillis(), ChatMessage.Type.RECEIVED)
                            mChatView!!.addMessage(chatMessage)
                        } else {
                            Log.d(TAG, "Got a message from jid :$from")
                        }
                        return
                    }
                }
            }
        }

        val filter = IntentFilter(XmppConnectionService.NEW_MESSAGE)
        registerReceiver(mBroadcastReceiver, filter)
    }

    companion object {
        private val TAG = "ChatActivity"
    }
}
