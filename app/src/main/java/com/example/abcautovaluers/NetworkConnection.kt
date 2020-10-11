package com.example.abcautovaluers

import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.*
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData

class NetworkConnection(private val context: Context): LiveData<Boolean>() {

    private var connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

    private val tag = "NetworkConnection"

    override fun onActive() {
        super.onActive()
        updateConnection()
        when {

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {

                connectivityManager.registerDefaultNetworkCallback(connectivityManagerCallbacks())

            }


            else -> {

                context.registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

            }
        }
    }


    override fun onInactive() {

        try {

            connectivityManager.unregisterNetworkCallback(connectivityManagerCallbacks())

        } catch (error: Exception){

            Log.d(tag, error.toString())

        }


        super.onInactive()

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun lollipopNetworkRequest(){

        val requestBuilder = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)

        connectivityManager.registerNetworkCallback(requestBuilder.build(), connectivityManagerCallbacks())

    }

    private fun connectivityManagerCallbacks(): ConnectivityManager.NetworkCallback {

        networkCallback = object: ConnectivityManager.NetworkCallback() {

            override fun onLost(network: Network) {
                super.onLost(network)
                postValue(false)
            }

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                postValue(true)
            }

        }
        return networkCallback

    }

    private val networkReceiver = object: BroadcastReceiver() {

        override fun onReceive(p0: Context?, p1: Intent?) {

            updateConnection()

        }

    }


    private fun updateConnection() {

        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        postValue(activeNetwork?.isConnected == true)

    }


}