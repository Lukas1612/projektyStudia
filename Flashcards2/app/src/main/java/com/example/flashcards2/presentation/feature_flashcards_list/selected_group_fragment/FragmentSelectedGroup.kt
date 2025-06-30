package com.example.flashcards2.presentation.feature_flashcards_list.selected_group_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.flashcards2.R
import com.example.flashcards2.presentation.Constants.GROUP_ID_KEY
import com.example.flashcards2.presentation.helpers.AlertDialogBuildHelper
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@WithFragmentBindings
@AndroidEntryPoint
class FragmentSelectedGroup : Fragment() {

    private lateinit var toolbar: Toolbar
    private lateinit var learnButton: Button
    private lateinit var cardsToLearnInfoTextView: TextView

    private val fragmentViewModel: SelectedGroupFragmentViewModel by viewModels()

    private val navigation: NavController by lazy {
        findNavController()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            fragmentViewModel.eventFlow.collectLatest { event ->
                when(event){
                    SelectedGroupFragmentViewModel.UiEvent.OpenEditGroupScreen -> {
                        openEditGroupScreen()
                    }

                    SelectedGroupFragmentViewModel.UiEvent.GoBackToFlashcardGroupsScreen -> {
                        navigation.popBackStack()
                    }

                    SelectedGroupFragmentViewModel.UiEvent.OpenLearningScreen -> {
                        openLearningScreen()
                    }

                    SelectedGroupFragmentViewModel.UiEvent.OpenDeletedItemsAlert -> {
                        doYouWantToRemoveSelectedElementsAlertDialog()
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_selected_group, container, false)
        toolbar = view.findViewById(R.id.toolbar)
        toolbar.addMenuProvider(createMenuProvider(), viewLifecycleOwner, Lifecycle.State.RESUMED)

        learnButton = view.findViewById(R.id.learnButton)
        cardsToLearnInfoTextView = view.findViewById(R.id.cardsToLearnInfoTextView)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        learnButton.setOnClickListener {
            fragmentViewModel.onEvent(SelectedGroupEvent.LearnButtonClicked)
        }

        lifecycleScope.launch {
            fragmentViewModel.state.collectLatest { state ->

                cardsToLearnInfoTextView.setText(state.numberOfCardsToReviewText)
                learnButton.isVisible = state.learnButtonVisibility
            }
        }

    }

    private fun createMenuProvider(): MenuProvider {
        return object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.fragment_selected_group_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.edit_group -> {
                        fragmentViewModel.onEvent(SelectedGroupEvent.EditButtonClicked)
                        true
                    }

                    R.id.delete_group -> {
                        fragmentViewModel.onEvent(SelectedGroupEvent.DeleteButtonClicked)
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun openEditGroupScreen(){
        fragmentViewModel.getGroupId()?.let { id ->
            val bundle = Bundle()
            bundle.putLong(GROUP_ID_KEY, id)

            navigation.navigate(R.id.action_FragmentSelectedGroup_to_FragmentFlashcardsList, bundle)
        }
    }

    private fun openLearningScreen(){
        fragmentViewModel.getGroupId()?.let { id ->
            val bundle = Bundle()
            bundle.putLong(GROUP_ID_KEY, id)

            val navOptions: NavOptions = NavOptions.Builder()
                .setPopUpTo(R.id.FragmentFlashcardGroupList, false, true)
                .setRestoreState(true)
                .build()

            navigation.navigate(R.id.action_FragmentSelectedGroup_to_FlashcardActivity, bundle, navOptions)
        }
    }

    private fun doYouWantToRemoveSelectedElementsAlertDialog(){
        AlertDialogBuildHelper().doYouWantToRemoveSelectedElementsAlert(
            requireContext()
        ) {
            fragmentViewModel.onEvent(SelectedGroupEvent.DeleteButtonConfirmed)
        }
    }

}