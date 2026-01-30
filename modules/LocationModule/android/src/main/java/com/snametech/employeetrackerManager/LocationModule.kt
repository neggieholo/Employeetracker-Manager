package com.snametech.employeetrackerManager

import android.content.Intent
import androidx.core.content.ContextCompat
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import kotlinx.coroutines.*
import android.util.Log

class LocationModule : Module() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun definition() = ModuleDefinition {

        Name("LocationModule")

        Events("onLocationUpdate")

        OnCreate {
            Log.d("LocationModule", "EXPO MODULE INITIALIZED")
            scope.launch {
                // LocationStore is now found locally
                LocationStore.location.collect { state ->
                    if (state != null) {
                        sendEvent(
                            "onLocationUpdate",
                            mapOf(
                                "latitude" to state.latitude,
                                "longitude" to state.longitude,
                                "address" to (state.address ?: "Searching...")
                            )
                        )
                    }
                }
            }
        }

        OnDestroy {
            scope.cancel()
        }

        Function("startTracking") {
            Log.d("LocationModule", "StartTracking function called from JS") // <--- NATIVE LOG
            
            val mContext = appContext.reactContext ?: run {
                Log.e("LocationModule", "Context is NULL!")
                return@Function false
            }
            
            val intent = Intent(mContext, LocationService::class.java)
            
            ContextCompat.startForegroundService(mContext, intent)
            Log.d("LocationModule", "Foreground Service intent sent") // <--- NATIVE LOG
            return@Function true
        }

        
        Function("stopTracking") {
            val mContext = appContext.reactContext ?: return@Function false
            val intent = Intent(mContext, LocationService::class.java)
            mContext.stopService(intent)
            return@Function true
        }
    } 
}