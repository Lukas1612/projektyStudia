package com.example.locationdescriptor.location_descriptor.presentation.add_edit_photo

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.locationdescriptor.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddEditDescriptionScreen : AppCompatActivity() {

    private val viewModel: AddEditDescriptionViewModel by viewModels()

    private lateinit var alertDialogBuilderHelper: AlertDialogBuilderHelper

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var imageView: ImageView? = null
    private var titleTextView: TextView? = null
    private var latitudeTextView: TextView? = null
    private var longitudeTextView: TextView? = null
    private var noteEditText: EditText? = null
    private var cancelButton: Button? = null
    private var saveButton: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_photo_screen)

        alertDialogBuilderHelper = AlertDialogBuilderHelper(this)

         imageView = findViewById(R.id.imageView)
         latitudeTextView = findViewById(R.id.latitudeTextView)
         longitudeTextView = findViewById(R.id.longitudeTextView)
         titleTextView = findViewById(R.id.titleTextView)
         titleTextView!!.movementMethod = ScrollingMovementMethod()
         noteEditText = findViewById(R.id.noteEditText)
         cancelButton = findViewById(R.id.cancelButton)
         saveButton = findViewById(R.id.okButton)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        saveButton!!.setOnClickListener{

            viewModel.onEvent(AddEditDescriptionEvent.EnteredNote(noteEditText?.text.toString()))

            viewModel.onEvent(AddEditDescriptionEvent.SaveButtonClicked)
        }

        cancelButton!!.setOnClickListener{
            viewModel.onEvent(AddEditDescriptionEvent.CancelButtonClicked)
        }




        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED)
            {
                 viewModel.state.collectLatest {
                    titleTextView?.text = it.title
                    noteEditText?.setText(it.note)

                    longitudeTextView?.text = it.longitude.toString()

                    latitudeTextView?.text = it.latitude.toString()

                   imageView?.setImageBitmap(it.photo)
                }
            }
        }



        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED)
            {
                viewModel.eventFlow.collect { event ->
                    when(event){
                        is AddEditDescriptionViewModel.UiEvent.FinishActivity -> {
                            finish()
                        }
                        is AddEditDescriptionViewModel.UiEvent.GetCurrentLocation -> {
                            getTheCurrentLocation()
                        }
                        is AddEditDescriptionViewModel.UiEvent.TakePhoto -> {
                            takePhoto()
                        }

                        is AddEditDescriptionViewModel.UiEvent.AskIfUserConfirmSavingThePhoto -> {
                            alertDialogBuilderHelper.createDoYouWantToSaveAlert {
                                viewModel.onEvent(AddEditDescriptionEvent.SaveConfirmed)
                            }
                        }

                        is AddEditDescriptionViewModel.UiEvent.AskIfUserConfirmToCancelWithoutSaving -> {
                            alertDialogBuilderHelper.createDoYouWantToCancelAlert {
                                viewModel.onEvent(AddEditDescriptionEvent.CancelConfirmed)
                            }
                        }
                    }
                }
            }
        }
    }


//**************** taking photo ******************

   private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result ->

        if(result.resultCode == Activity.RESULT_OK)
        {
            val data: Intent? = result.data
            val photo = data?.extras?.get("data") as Bitmap
            viewModel.onEvent(AddEditDescriptionEvent.TakenPhoto(photo))
        }else
        {
        }
    }

    private fun takePhoto()
    {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncher.launch(cameraIntent)
    }
//**********************************************

    @SuppressLint("MissingPermission")
    private fun getTheCurrentLocation() {

        if(isGPSEnabled()){
            fusedLocationProviderClient
                .lastLocation.addOnSuccessListener { location: Location? ->

                    viewModel.onEvent(AddEditDescriptionEvent.EnteredLatitude(location?.latitude))
                    viewModel.onEvent(AddEditDescriptionEvent.EnteredLongitude(location?.longitude))

                    if(location?.latitude != null && location.longitude != null)
                    {
                        createPictureTitle(location.latitude, location.longitude)
                    }
                }
        }else
        {
            makeGpsDisabledAlertDialog()
        }
    }

    private fun makeGpsDisabledAlertDialog()
    {
        val builder = AlertDialog.Builder(this)

        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes"){ dialog, which ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .setNegativeButton("No"){dialog, which ->
                dialog.cancel()
            }
        builder.show()
    }

    private fun createPictureTitle(latitude: Double, longitude: Double)
   {
       Geocoder(this, Locale("pl"))
           .getAddress(latitude, longitude) { address: android.location.Address? ->
               if (address != null) {

                   val sdf = SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale("pl"))
                   val currentDateAndTime = sdf.format(Date())

                   var title = "" + address.locality + "_" + address.countryName + "_" + currentDateAndTime
                   title = title.replace(' ', '_')


                   viewModel.onEvent(AddEditDescriptionEvent.EnteredTitle(title))

               }
           }
   }

    private fun isGPSEnabled(): Boolean
    {
        val locationManager: LocationManager? = getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }



    @Suppress("DEPRECATION")
    fun Geocoder.getAddress(
        latitude: Double,
        longitude: Double,
        address: (android.location.Address?) -> Unit
    ) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getFromLocation(latitude, longitude, 1) { address(it.firstOrNull()) }
            return
        }

        try {
            address(getFromLocation(latitude, longitude, 1)?.firstOrNull())
        } catch(e: Exception) {
            //will catch if there is an internet problem
            address(null)
        }
    }
}