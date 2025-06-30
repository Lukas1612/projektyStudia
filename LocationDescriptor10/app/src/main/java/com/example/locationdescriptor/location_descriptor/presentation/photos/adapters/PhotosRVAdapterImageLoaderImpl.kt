package com.example.locationdescriptor.location_descriptor.presentation.photos.adapters

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy

class PhotosRVAdapterImageLoaderImpl(private val context: Context, private val imagePath: String): PhotosRVAdapterImageLoader {
    private var glide: RequestManager = Glide.with(context)

    override fun loadImageToThumbnail(thumbnail: ImageView, photoName: String) {

        val completePath = "$imagePath/$photoName.jpg"

        glide.asBitmap()
            .load(completePath)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .sizeMultiplier(0.4f)
            .into(thumbnail)
    }
}