package com.example.locationdescriptor.location_descriptor.domain.use_case.photo

data class PhotoUseCases(
    val savePhoto: SavePhoto,
    val deletePhoto: DeletePhoto,
    val getPhoto: GetPhoto,
    val getPhotoPath: GetPhotoDirectoryPath,
    val doesFileExist: DoesFileExist,
    val savePdfToFile: SavePdfToFile
)
