package com.example.flashcards2.presentation.feature_flashcards_list.groups_fragment.permissions

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class PermissionHelper(
    private val context: Context,
    private val componentActivity: ComponentActivity,
) : DefaultLifecycleObserver  {

    private val arrayOfPermissions =  arrayOf(
        Manifest.permission.POST_NOTIFICATIONS)

    // alert dialog, informing that the permissions are required, showing after a user denied the permissions for the first time
    private lateinit var permissionDeniedAlertDialogBuilder : AlertDialog.Builder

    lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(owner: LifecycleOwner) {

        initPermissionDeniedAlertDialogBuilder()
        initRequestPermissionLauncher()

    }

    private fun initPermissionDeniedAlertDialogBuilder() {
        permissionDeniedAlertDialogBuilder = AlertDialog.Builder(context)
            .setMessage("the app needs NOTIFICATION permissions. " +
                    "if you want to get notification if there are flashcards to learn")
            .setTitle("permissions needed")
            .setCancelable(false)
            .setNegativeButton("cancel"){ dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("ok"){dialog, which ->

                openAppSettings()
                dialog.dismiss()
            }
    }

    // the request asks for the permissions for the FIRST time
    private fun initRequestPermissionLauncher(){
        requestPermissionLauncher = with(componentActivity){
            registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) {
                    permissions ->
                val isGranted = permissions.entries.all { it.value == true}

                if(isGranted){
                }else
                {
                    requestPermissions()
                }

            }
        }
    }

    fun requestPermissions()
    {
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED -> {
            }
            shouldShowRequestPermissionRationale() -> {
                permissionDeniedAlertDialogBuilder.show()
        }
            else -> {
                requestPermissionLauncher.launch(
                    arrayOfPermissions)
            }
        }
    }


    private fun shouldShowRequestPermissionRationale(): Boolean
    {
        return ActivityCompat.shouldShowRequestPermissionRationale(
            componentActivity,
            Manifest.permission.POST_NOTIFICATIONS
        )
    }

    private fun openAppSettings() {
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null)
        ).also { context.startActivity(it)}
    }

    fun permissionsAccepted(): Boolean{
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }
}