package com.example.locationdescriptor.location_descriptor.domain.use_case.description

import com.example.locationdescriptor.location_descriptor.domain.model.PhotoDescription
import com.example.locationdescriptor.location_descriptor.domain.repository.DescriptionRepository


class GetPhotoDescription(private val repository: DescriptionRepository) {

    suspend operator fun invoke(id: Int): PhotoDescription?
    {
        return repository.getPhotoById(id)
    }
}