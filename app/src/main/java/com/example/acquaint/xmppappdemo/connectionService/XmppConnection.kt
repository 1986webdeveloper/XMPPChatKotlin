package com.example.acquaint.xmppappdemo.connectionService

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log

import com.example.acquaint.xmppappdemo.R

import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.ConnectionListener
import org.jivesoftware.smack.ReconnectionManager
import org.jivesoftware.smack.SmackException
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.XMPPException
import org.jivesoftware.smack.chat2.Chat
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.chat2.IncomingChatMessageListener
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smack.packet.Presence
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jxmpp.jid.EntityBareJid
import org.jxmpp.jid.impl.JidCreate
import org.jxmpp.stringprep.XmppStringprepException

import java.io.IOException
import java.net.InetAddress
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession

class XmppConnection(context: Context) {
    private val mApplicationContext: Context
    private var uiThreadMessageReceiver: BroadcastReceiver? = null//Receives messages from the ui thread.

    /**
     * Listener to receive the connection events
     */
    private val mOnConnectionListener = object : ConnectionListener {
        override fun connected(connection: XMPPConnection) {
            XmppConnectionService.sConnectionState = ConnectionState.CONNECTED
            Log.d(TAG, "Connected Successfully")
        }

        override fun authenticated(connection: XMPPConnection, resumed: Boolean) {
            XmppConnectionService.sConnectionState = ConnectionState.CONNECTED
            Log.d(TAG, "Authenticated Successfully")
            showContactListActivityWhenAuthenticated()
        }

        override fun connectionClosed() {
            XmppConnectionService.sConnectionState = ConnectionState.DISCONNECTED
            Log.d(TAG, "Connectionclosed()")
        }

        override fun connectionClosedOnError(e: Exception) {
            XmppConnectionService.sConnectionState = ConnectionState.DISCONNECTED
            Log.d(TAG, "ConnectionClosedOnError, error $e")

        }

        override fun reconnectionSuccessful() {
            XmppConnectionService.sConnectionState = ConnectionState.CONNECTED
            Log.d(TAG, "ReconnectionSuccessful()")
        }

        override fun reconnectingIn(seconds: Int) {
            XmppConnectionService.sConnectionState = ConnectionState.CONNECTING
            Log.d(TAG, "ReconnectingIn() ")
        }

        override fun reconnectionFailed(e: Exception) {
            XmppConnectionService.sConnectionState = ConnectionState.DISCONNECTED
            Log.d(TAG, "ReconnectionFailed()")
        }
    }

    init {
        Log.d(TAG, "XmppConnection Constructor called.")
        mApplicationContext = context.applicationContext
    }

    /**
     * method to connect the client to the server
     * @throws IOException
     * @throws XMPPException
     * @throws SmackException
     */
    @Throws(IOException::class, XMPPException::class, SmackException::class)
    fun connect() {
            var addr = InetAddress.getByName("192.168.1.44")
         var verifier = object :HostnameVerifier{
             override fun verify(hostname: String?, session: SSLSession?): Boolean {
                 return false
             }
         }
            val conf = XMPPTCPConnectionConfiguration.builder()
                .setXmppDomain(mApplicationContext.getString(R.string.txt_domain_name)) // name of the domain
                .setHost(mApplicationContext.getString(R.string.txt_server_address)) // address of the server
                .setResource(mApplicationContext.getString(R.string.txt_resource)) // resource from where your request is sent
                .setPort(5222) // static port number to connect
                .setKeystoreType(null) //To avoid authentication problem. Not recommended for production build
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                    .setHostAddress(addr)
                    .setHostnameVerifier(verifier)
                    .setDebuggerEnabled(true)
                .setCompressionEnabled(true).build()

        //Set up the ui thread broadcast message receiver.
        setupUiThreadBroadCastMessageReceiver()

        mConnection = XMPPTCPConnection(conf)
        mConnection!!.addConnectionListener(mOnConnectionListener)
        try {
            Log.d(TAG, "Calling connect() ")
            mConnection!!.connect()
            val presence = Presence(Presence.Type.available)
            mConnection!!.sendPacket(presence)

            Log.d(TAG, " login() Called ")
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        /**
         * Listener to receive the incoming message
         */

        ChatManager.getInstanceFor(mConnection).addIncomingListener { messageFrom, message, chat ->
            val from = message.from.toString()

            var contactJid = ""
            if (from.contains("/")) {
                contactJid = from.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
                Log.d(TAG, "The real jid is :$contactJid")
                Log.d(TAG, "The message is from :$from")
            } else {
                contactJid = from
            }

            //Bundle up the intent and send the broadcast.
            val intent = Intent(XmppConnectionService.NEW_MESSAGE)
            intent.setPackage(mApplicationContext.packageName)
            intent.putExtra(XmppConnectionService.BUNDLE_FROM_JID, contactJid)
            intent.putExtra(XmppConnectionService.BUNDLE_MESSAGE_BODY, message.body)
            mApplicationContext.sendBroadcast(intent)
            Log.d(TAG, "Received message from :$contactJid broadcast sent.")
        }


        val reconnectionManager = ReconnectionManager.getInstanceFor(mConnection)
        ReconnectionManager.setEnabledPerDefault(true)
        reconnectionManager.enableAutomaticReconnection()

    }

    /**
     * broadcast method to send message from one client to another
     */
    private fun setupUiThreadBroadCastMessageReceiver() {
        uiThreadMessageReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                //Check if the Intents purpose is to send the message.
                val action = intent.action
                if (action == XmppConnectionService.SEND_MESSAGE) {
                    //Send the message.
                    sendMessage(intent.getStringExtra(XmppConnectionService.BUNDLE_MESSAGE_BODY),
                            intent.getStringExtra(XmppConnectionService.BUNDLE_TO))
                }
            }
        }

        val filter = IntentFilter()
        filter.addAction(XmppConnectionService.SEND_MESSAGE)
        mApplicationContext.registerReceiver(uiThreadMessageReceiver, filter)

    }

    /**
     * method to send message from one client to another
     * @param body -- message to be sent
     * @param toJid -- id of the receiver
     */
    private fun sendMessage(body: String, toJid: String) {
        var toJid = toJid
        Log.d(TAG, "Sending message to :$toJid")

        var jid: EntityBareJid? = null

        toJid = toJid + "@" + mApplicationContext.getString(R.string.txt_domain_name) + "/" + mApplicationContext.getString(R.string.txt_resource)
        val chatManager = ChatManager.getInstanceFor(mConnection)

        try {
            jid = JidCreate.entityBareFrom(toJid)
            Log.e(TAG, "sendMessage: jid : " + jid!!)
        } catch (e: XmppStringprepException) {
            e.printStackTrace()
        }

        val chat = chatManager.chatWith(jid)
        try {
            val message = Message(jid, Message.Type.chat)
            message.body = body
            chat.send(message)

        } catch (e: SmackException.NotConnectedException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

    /**
     * method to disconnect the user from the server
     */
    fun disconnect() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(mApplicationContext)
        prefs.edit().putBoolean("xmpp_logged_in", false).commit()

        if (mConnection != null) {
            mConnection!!.disconnect()
        }

        mConnection = null
        // Unregister the message broadcast receiver.
        if (uiThreadMessageReceiver != null) {
            mApplicationContext.unregisterReceiver(uiThreadMessageReceiver)
            uiThreadMessageReceiver = null
        }
    }

    private fun showContactListActivityWhenAuthenticated() {
        val i = Intent(XmppConnectionService.UI_AUTHENTICATED)
        i.setPackage(mApplicationContext.packageName)
        mApplicationContext.sendBroadcast(i)
    }


    enum class ConnectionState {
        CONNECTED, AUTHENTICATED, CONNECTING, DISCONNECTING, DISCONNECTED
    }

    enum class LoggedInState {
        LOGGED_IN, LOGGED_OUT
    }

    companion object {

        private val TAG = "XmppConnection"
        private var mConnection: XMPPTCPConnection? = null

        val instance: XMPPConnection?
            get() = mConnection

        fun login(username: String, password: String) {
            try {
                mConnection!!.login(username, password)
            } catch (e: XMPPException) {
                e.printStackTrace()
            } catch (e: SmackException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

        }
    }
}
