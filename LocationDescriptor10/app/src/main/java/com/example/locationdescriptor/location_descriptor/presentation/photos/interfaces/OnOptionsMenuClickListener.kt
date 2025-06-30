package com.example.locationdescriptor.location_descriptor.presentation.photos.interfaces

import com.example.locationdescriptor.location_descriptor.presentation.photos.adapters.ListItem

interface OnOptionsMenuClickListener {
    fun onDelete(item: ListItem)
    fun onSelect(item: ListItem)
}