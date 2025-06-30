package com.example.locationdescriptor.location_descriptor.domain.use_case.description

data class PhotoDescriptionUseCases(
    val addPhotoDescription: AddPhotoDescription,
    val deletePhotoDescription: DeletePhotoDescription,
    val getPhotoDescription: GetPhotoDescription,
    val getPhotoDescriptions: GetPhotoDescriptions
)
