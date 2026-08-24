package com.gfk.s2s.demo.s2s.helper

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat

object PermissionsHelper {
    fun requestLocalhostPermission(activity: Activity, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CINNAMON_BUN) {
            when {
                ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_LOCAL_NETWORK) == PackageManager.PERMISSION_GRANTED -> {
                    // Permission is already granted. Handling not needed
                }
                shouldShowRequestPermissionRationale(activity, android.Manifest.permission.ACCESS_LOCAL_NETWORK) -> {
                    // Show an educational UI explaining why you need the permission
                    AlertDialog.Builder(activity)
                        .setTitle("Permission Required")
                        .setMessage("Please grant Local Network Permission.\nSensic SDK requires this permission to associate the app usage with the panelist user. Without this permission, the SDK will not be able to associate the app usage with the panelist user.")
                        .setPositiveButton("Yes") { dialog, _ ->
                            // After user clicks OK, launch the permission request
                            dialog.dismiss()
                            ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.ACCESS_LOCAL_NETWORK), requestCode)
                        }
                        .setNegativeButton("No") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
                ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_LOCAL_NETWORK) != PackageManager.PERMISSION_GRANTED -> {
                    // Directly ask for the permission
                    // Show an educational UI explaining why you need the permission
                    AlertDialog.Builder(activity)
                        .setTitle("Permission Required")
                        .setMessage("Please grant Local Network Permission. Sensic SDK requires this permission for sharing app usage with the panelist application installed.")
                        .setPositiveButton("OK") { dialog, _ ->
                            // After user clicks OK, launch the permission request
                            dialog.dismiss()
                            ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.ACCESS_LOCAL_NETWORK), requestCode)
                        }
                        .show()
                }
            }
        }
    }
}

/**
 * Checks if an application is installed on the device.
 * @param packageName The unique package ID of the target app (e.g., "com.whatsapp")
 * @return True if installed, false otherwise.
 */
fun Context.isAppInstalled(packageName: String): Boolean {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Modern API flag handling for Android 13 (API 33) and above
            packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
        } else {
            // Legacy API handling for Android 12 and below
            @Suppress("DEPRECATION")
            packageManager.getPackageInfo(packageName, 0)
        }
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}