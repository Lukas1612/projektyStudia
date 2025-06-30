package com.example.locationdescriptor.location_descriptor.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.locationdescriptor.R
import com.example.locationdescriptor.location_descriptor.presentation.photos.PhotosScreen
import com.example.locationdescriptor.location_descriptor.presentation.photos.folders_tree.MakeTreeTest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var makeTreeTest: MakeTreeTest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       // makeTreeTest = MakeTreeTest()

        startActivity(Intent(this, PhotosScreen::class.java))

        finish()

    }

}