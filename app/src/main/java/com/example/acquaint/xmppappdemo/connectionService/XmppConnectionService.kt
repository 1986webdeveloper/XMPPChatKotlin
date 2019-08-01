package com.example.acquaint.xmppappdemo.connectionService

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log

import org.jivesoftware.smack.SmackException
import org.jivesoftware.smack.XMPPException

import java.io.IOException

class XmppConnectionService : Service() {
    private var mActive: Boolean = false//Stores whether or not the thread is active
    private var mThread: Thread? = null
    private var mTHandler: Handler? = null//We use this handler to post messages to
    //the background thread.
    private var mConnection: XmppConnection? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate()")
    }

    /**
     * initialise connection to the server
     */
    private fun initConnection() {
        Log.d(TAG, "initConnection()")
        if (mConnection == null) {
            mConnection = XmppConnection(this)
        }
        try {
            mConnection!!.connect()

        } catch (e: IOException) {
            Log.d(TAG, "Something went wrong while connecting ,make sure the credentials are right and try again")
            e.printStackTrace()
            //Stop the service all together.
            stopSelf()
        } catch (e: SmackException) {
            Log.d(TAG, "Something went wrong while connecting ,make sure the credentials are right and try again")
            e.printStackTrace()
            stopSelf()
        } catch (e: XMPPException) {
            Log.d(TAG, "Something went wrong while connecting ,make sure the credentials are right and try again")
            e.printStackTrace()
            stopSelf()
        }

    }


    fun start() {
        Log.d(TAG, " Service Start() function called.")
        if (!mActive) {
            mActive = true
            if (mThread == null || !mThread!!.isAlive) {
                mThread = Thread(Runnable {
                    Looper.prepare()
                    mTHandler = Handler()
                    initConnection()
                    //THE CODE HERE RUNS IN A BACKGROUND THREAD.
                    Looper.loop()
                })
                mThread!!.start()
            }
        }
    }

    fun stop() {
        Log.d(TAG, "stop()")
        mActive = false
        mTHandler!!.post {
            if (mConnection != null) {
                mConnection!!.disconnect()
            }
        }

    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand()")
        start()
        return Service.START_STICKY
        //RETURNING START_STICKY CAUSES OUR CODE TO STICK AROUND WHEN THE APP ACTIVITY HAS DIED.
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy()")
        super.onDestroy()
        stop()
    }

    companion object {
        val UI_AUTHENTICATED = "com.example.acquaint.xmppappdemo.uiauthenticated"
        val SEND_MESSAGE = "com.example.acquaint.xmppappdemo.sendmessage"
        val BUNDLE_MESSAGE_BODY = "b_body"
        val BUNDLE_TO = "b_to"
        val NEW_MESSAGE = "com.example.acquaint.xmppappdemo.newmessage"
        val BUNDLE_FROM_JID = "b_from"
        private val TAG = "RoosterService"
        var sConnectionState: XmppConnection.ConnectionState? = null
        var sLoggedInState: XmppConnection.LoggedInState? = null

        /**
         * method to get the connection
         *
         * @return - state of the connection
         */
        val state: XmppConnection.ConnectionState
            get() = (if (sConnectionState == null) {
                XmppConnection.ConnectionState.DISCONNECTED
            } else sConnectionState)!!

        val loggedInState: XmppConnection.LoggedInState
            get() = (if (sLoggedInState == null) {
                XmppConnection.LoggedInState.LOGGED_OUT
            } else sLoggedInState)!!
    }

}
