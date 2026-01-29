package com.snametech.employeetrackerManager

import android.content.Intent
import androidx.core.content.ContextCompat
import com.simonholo.EmployeetrackerManager.LocationService
import com.simonholo.EmployeetrackerManager.LocationStore
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import kotlinx.coroutines.*

class LocationModule : Module() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun definition() = ModuleDefinition {

        Name("LocationModule")

        Events("onLocationUpdate")

        OnCreate {
            scope.launch {
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
            val intent = Intent(context, LocationService::class.java)
            ContextCompat.startForegroundService(context, intent)
        }

        Function("stopTracking") {
            context.stopService(Intent(context, LocationService::class.java))
        }
    }
}
