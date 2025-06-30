package com.example.locationdescriptor.location_descriptor.presentation.photos

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.locationdescriptor.R
import com.example.locationdescriptor.location_descriptor.domain.model.PhotoDescription
import com.example.locationdescriptor.location_descriptor.presentation.add_edit_photo.AddEditDescriptionScreen
import com.example.locationdescriptor.location_descriptor.presentation.photos.adapters.ListItem
import com.example.locationdescriptor.location_descriptor.presentation.photos.adapters.PhotosRVAdapterImageLoaderImpl
import com.example.locationdescriptor.location_descriptor.presentation.photos.adapters.PhotosRecyclerViewAdapter
import com.example.locationdescriptor.location_descriptor.presentation.photos.interfaces.*
import com.example.locationdescriptor.location_descriptor.presentation.photos.permissions.PermissionHelper
import com.example.locationdescriptor.location_descriptor.presentation.photos.permissions.PermissionsRequestListener
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class PhotosScreen : AppCompatActivity() {

    private lateinit var photosRecyclerView: RecyclerView
    private lateinit var photosRecyclerViewAdapter: PhotosRecyclerViewAdapter
    private lateinit var mainFab: FloatingActionButton
    private lateinit var photoFab: FloatingActionButton
    private lateinit var folderFab: FloatingActionButton
    private lateinit var bottomAppBar: BottomAppBar
    private lateinit var imageLoader: PhotosRVAdapterImageLoaderImpl
    private lateinit var photoDirectoryPath: String

    private lateinit var permissionHelper: PermissionHelper
    private lateinit var alertDialogBuilderHelper: AlertDialogBuilderHelper

    private val viewModel: PhotosViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photos_screen)

        permissionHelper = PermissionHelper(this, this)
        lifecycle.addObserver(permissionHelper)


        alertDialogBuilderHelper = AlertDialogBuilderHelper(this)

        photosRecyclerView = findViewById(R.id.photosRecyclerView)
        photosRecyclerView.layoutManager = GridLayoutManager(this, 2)
        mainFab = findViewById(R.id.main_fab)
        folderFab = findViewById(R.id.add_folder_fab)
        photoFab = findViewById(R.id.take_photo_fab)
        bottomAppBar = findViewById(R.id.bottom_app_bar)

        folderFab.visibility = View.GONE
        photoFab.visibility = View.GONE

        bottomAppBar.menu.get(0).isVisible = false

        photoDirectoryPath = viewModel.getPhotoDirectoryPath()!!

        imageLoader = PhotosRVAdapterImageLoaderImpl(applicationContext, photoDirectoryPath)

        initRecyclerViewAdapter()

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED)
            {
                viewModel.state.collectLatest {
                    updateRecyclerViewAdapter()
                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED)
            {
                viewModel.eventFlow.collectLatest {event ->
                    when(event)
                    {
                        is PhotosViewModel.UiEvent.OpenPhotoScreen -> {
                            permissionHelper.requestPermissions(object :
                                PermissionsRequestListener {
                                override fun doOnGranted() {
                                    openPhotoScreen(event.id, event.folderName)
                                }
                                })
                        }

                        is PhotosViewModel.UiEvent.FolderNameExistWindow -> {

                            alertDialogBuilderHelper.createFolderNameAlreadyExistsAlert()
                        }

                        is PhotosViewModel.UiEvent.FileNameExistWindow -> {
                            alertDialogBuilderHelper.createFileNameAlreadyExistsAlert()
                        }

                        is PhotosViewModel.UiEvent.OpenFolderCreationPopUp -> {

                            alertDialogBuilderHelper.createNewFolderCreationWindow { folderName ->
                                viewModel.onEvent(PhotosEvent.OnNewFolderCreated(folderName))
                            }
                        }

                        is PhotosViewModel.UiEvent.FinishApp -> {
                            finish()
                        }

                        is PhotosViewModel.UiEvent.CollapseFab ->
                        {
                            folderFab.hide()
                            photoFab.hide()
                        }

                        is PhotosViewModel.UiEvent.ExpandFab ->
                        {
                            folderFab.show()
                            photoFab.show()
                        }

                        is PhotosViewModel.UiEvent.UpdateRecyclerView -> {
                            updateRecyclerView()
                        }
                        is PhotosViewModel.UiEvent.ChangeMultiDeleteButtonVisibility -> {

                            changeMultiDeleteButtonVisibility(event.value)
                        }

                        is PhotosViewModel.UiEvent.ChangeSelectionClearButtonVisibility -> {

                            changeSelectionClearButtonVisibility(event.value)
                        }

                        is PhotosViewModel.UiEvent.ChangeMoveFoldersButtonVisibility ->
                        {
                            changeMoveFoldersButtonVisibility(event.value)

                        }

                        is PhotosViewModel.UiEvent.ShowMultiDeleteAlertWindow ->
                        {
                            alertDialogBuilderHelper.createDeleteItemsAskingWarningWindow {
                                viewModel.onEvent(PhotosEvent.OnMultipleDeleteConfirmedByUser)
                            }
                        }

                        is PhotosViewModel.UiEvent.OpenNonDataSelectedPopUp -> {

                            alertDialogBuilderHelper.createNonDataSelectedAlertWindow()
                        }
                    }

                }
            }
        }


        mainFab.setOnClickListener{
            viewModel.onEvent(PhotosEvent.MainFabButtonCLicked)
        }


        photoFab.setOnClickListener {
            viewModel.onEvent(PhotosEvent.PhotoFabButtonCLicked)
        }


        folderFab.setOnClickListener {

            viewModel.onEvent(PhotosEvent.FolderFabButtonCLicked)
        }


        bottomAppBar.setOnMenuItemClickListener { menuItem ->

            when(menuItem.itemId)
            {
                R.id.clear_all_selected -> {
                    viewModel.onEvent(PhotosEvent.OnItemsSelectionCleared)
                    true
                }
                R.id.multi_delete -> {
                    viewModel.onEvent(PhotosEvent.OnMultipleDeleteClicked)
                    true
                }

                R.id.move -> {
                    viewModel.onEvent(PhotosEvent.OnMove)
                    true
                }

                R.id.extract_pdf -> {

                    alertDialogBuilderHelper.createPdfConversionWindow { fileName ->
                        viewModel.onEvent(PhotosEvent.OnCreatePdf(fileName))
                    }

                    true
                }

                else -> {
                    false
                }
            }

        }

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.onEvent(PhotosEvent.OnBackPressed)
            }
        })

    }

    private fun changeMoveFoldersButtonVisibility(visibility: Boolean) {
        bottomAppBar.menu.get(2).isVisible = visibility
    }

    private fun changeSelectionClearButtonVisibility(visibility: Boolean) {
        bottomAppBar.menu.get(0).isVisible = visibility
    }

    private fun changeMultiDeleteButtonVisibility(visibility: Boolean) {
        bottomAppBar.menu.get(1).isVisible = visibility
    }

    private fun updateRecyclerView() {
        val items = viewModel.state.value.items
        photosRecyclerViewAdapter.saveData(items)
        photosRecyclerViewAdapter.notifyDataSetChanged()
    }


    private fun initRecyclerViewAdapter()
    {
        val items = viewModel.state.value.items
        photosRecyclerViewAdapter = PhotosRecyclerViewAdapter(
            imageLoader = imageLoader,
            object : OnPhotoClickListener{
                override fun onClick(photoDescription: PhotoDescription) {
                    viewModel.onEvent(PhotosEvent.OnPhotoClicked(photoDescription))
                }
            },
            object : OnFolderClickListener{
                override fun onClick(name: String) {
                    viewModel.onEvent(PhotosEvent.OnFolderClicked(name))
                }
            },

            object : OnParentClickListener{
                override fun onClick() {
                    viewModel.onEvent(PhotosEvent.OnParentFolderClicked)
                }
            },
            object : OnOptionsMenuClickListener{
                override fun onDelete(item: ListItem) {
                    viewModel.onEvent(PhotosEvent.OnDelete(item))
                }

                override fun onSelect(item: ListItem) {
                    viewModel.onEvent(PhotosEvent.OnSelect(item))
                }
            }

        )
        photosRecyclerViewAdapter.saveData(items)
        photosRecyclerView.adapter = photosRecyclerViewAdapter


    }

    private fun updateRecyclerViewAdapter()
    {
        val items = viewModel.state.value.items
        photosRecyclerViewAdapter.saveData(items)
    }

    private fun openPhotoScreen(photoId: Int, folderName: String)
    {
        val intent = Intent(applicationContext, AddEditDescriptionScreen::class.java)
        intent.putExtra("id", photoId)
        intent.putExtra("folder_name", folderName)
        startActivity(intent)
    }

}