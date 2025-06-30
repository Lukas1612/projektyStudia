package com.example.locationdescriptor.location_descriptor.presentation.photos.permissions

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
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION)

    // alert dialog, informing that the permissions are required, showing after a user denied the permissions for the first time
    private lateinit var permissionDeniedAlertDialogBuilder : AlertDialog.Builder

    private var permissionsRequestListener: PermissionsRequestListener? = null

    lateinit var requestPermissionsLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(owner: LifecycleOwner) {


        // alert dialog, informing that the permissions are required, showing after a user denied the permissions for the first time
        permissionDeniedAlertDialogBuilder = AlertDialog.Builder(context)
            .setMessage("the app requires CAMERA and LOCATION permissions. " +
                "You can go to the app settings to grant it.")
            .setTitle("permissions required")
            .setCancelable(false)
            .setNegativeButton("cancel"){ dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("ok"){dialog, which ->

                openAppSettings()
                dialog.dismiss()
            }

        // the request asks for the permissions for the FIRST time
          requestPermissionsLauncher = with(componentActivity){
            registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) {
                permissions ->
                val isGranted = permissions.entries.all { it.value == true}

                if(isGranted){
                }else
                {
                    requestPermissions(permissionsRequestListener)
                }


            }
        }

    }

    fun permissionsAccepted(): Boolean{
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }


      fun requestPermissions(permissionsRequestListener: PermissionsRequestListener?)
     {
         this.permissionsRequestListener = permissionsRequestListener

         when {
             checkSelfPermission() -> {
                 permissionsRequestListener!!.doOnGranted()
             }
             shouldShowRequestPermissionRationale() -> {
                 permissionDeniedAlertDialogBuilder.show()
             }
             else -> {
                 // You can directly ask for the permission.
                 // The registered ActivityResultCallback gets the result of this request.
                 requestPermissionsLauncher.launch(
                     arrayOfPermissions
                 )
             }
         }
     }


    private fun shouldShowRequestPermissionRationale(): Boolean
    {
        return ActivityCompat.shouldShowRequestPermissionRationale(
            componentActivity,
            Manifest.permission.CAMERA
        )  &&  ActivityCompat.shouldShowRequestPermissionRationale(
            componentActivity,
            Manifest.permission.ACCESS_FINE_LOCATION
        )  && ActivityCompat.shouldShowRequestPermissionRationale(
            componentActivity,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    private fun checkSelfPermission(): Boolean
    {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED &&  ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&  ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }


    private fun openAppSettings() {
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null)
        ).also { context.startActivity(it)}
    }







}