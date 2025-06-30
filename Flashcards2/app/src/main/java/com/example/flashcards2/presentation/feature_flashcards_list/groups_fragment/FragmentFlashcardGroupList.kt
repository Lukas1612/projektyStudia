package com.example.flashcards2.presentation.feature_flashcards_list.groups_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flashcards2.R
import com.example.flashcards2.databinding.FragmentFlashcardGroupListBinding
import com.example.flashcards2.presentation.Constants.GROUP_ID_KEY
import com.example.flashcards2.presentation.Constants.NULL_FLASHCARD_ID
import com.example.flashcards2.presentation.Constants.NULL_GROUP_ID
import com.example.flashcards2.presentation.StartActivityHelper
import com.example.flashcards2.presentation.feature_flashcards_list.groups_fragment.adapters.FlashcardsGroupsRecyclerViewAdapter
import com.example.flashcards2.presentation.feature_flashcards_list.groups_fragment.adapters.GroupAdapterItem
import com.example.flashcards2.presentation.feature_flashcards_list.groups_fragment.adapters.OnFlashcardGroupItemClickListener
import com.example.flashcards2.presentation.feature_flashcards_list.groups_fragment.permissions.PermissionHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@WithFragmentBindings
@AndroidEntryPoint
class FragmentFlashcardGroupList : Fragment() {

    private var _binding: FragmentFlashcardGroupListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var permissionHelper: PermissionHelper

    private lateinit var groupListRecyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var fabCreateFlashcard: FloatingActionButton
    private lateinit var fabCreateGroup: FloatingActionButton
    private lateinit var emptyListNoticeTextView: TextView
    private lateinit var flashcardsGroupsRecyclerViewAdapter: FlashcardsGroupsRecyclerViewAdapter
    private lateinit var toolbar: Toolbar

    private lateinit var navController: NavController

    private val fragmentViewModel: FragmentFlashcardGroupListViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionHelper = PermissionHelper(requireContext(), requireActivity())
        lifecycle.addObserver(permissionHelper)

        lifecycleScope.launch {
            fragmentViewModel.eventFlow.collectLatest { event ->
                when(event){

                    is FragmentFlashcardGroupListViewModel.UiEvent.GoToFlashcardCreatorScreen -> {
                         StartActivityHelper().startFlashcardCreatorActivity(
                             activity = activity,
                             flashcardId = NULL_FLASHCARD_ID,
                             groupId = NULL_GROUP_ID
                         )
                    }

                    is FragmentFlashcardGroupListViewModel.UiEvent.GoToFlashcardListFragment -> {
                        goToSelectedGroup(event.groupId)
                    }

                    is FragmentFlashcardGroupListViewModel.UiEvent.OpenGroupCreatorScreen -> {
                        StartActivityHelper().startGroupCreatorActivity(activity)
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFlashcardGroupListBinding.inflate(inflater, container, false)

        navController =  findNavController()

        toolbar = binding.toolbar
        toolbar.addMenuProvider(createMenuProvider(), viewLifecycleOwner, Lifecycle.State.RESUMED)

        fab = binding.fab
        fabCreateFlashcard = binding.fabCreateFlashcard
        fabCreateGroup = binding.fabCreateGroup

        initRecyclerView(inflater)

        
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        lifecycleScope.launch {

            fragmentViewModel.state.collectLatest { state ->

                fabCreateFlashcard.isVisible = state.expandedButtonsVisibility
                fabCreateGroup.isVisible =  state.expandedButtonsVisibility

                toolbar.isVisible = state.isToolBarVisible

                updateRecyclerViewAdapter(state.items)
                groupListRecyclerView.isVisible = state.recyclerViewVisibility
                emptyListNoticeTextView.isVisible = state.emptyListNoticeVisibility
            }
        }

        fab.setOnClickListener {
            fragmentViewModel.onEvent(FlashcardGroupListEvent.ClickedMainFabButton)
        }

        fabCreateGroup.setOnClickListener {
            fragmentViewModel.onEvent(FlashcardGroupListEvent.ClickedGroupFabButton)
        }

        fabCreateFlashcard.setOnClickListener {
            fragmentViewModel.onEvent(FlashcardGroupListEvent.ClickedFlashcardFabButton)
        }

        requestPermissions()
    }

    private fun createMenuProvider(): MenuProvider{
        return object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.fragment_flashcard_groups_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.delete_item -> {
                        fragmentViewModel.onEvent(FlashcardGroupListEvent.ClickedDeleteMenuItem)
                        true
                    }

                    R.id.clear_items -> {
                        fragmentViewModel.onEvent(FlashcardGroupListEvent.ClickedClearMenuItem)
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun initRecyclerView(inflater: LayoutInflater) {
        flashcardsGroupsRecyclerViewAdapter = createRecyclerViewAdapter()

        groupListRecyclerView = binding.groupListRecyclerView
        emptyListNoticeTextView =  binding.emptyListNoticeTextView
        groupListRecyclerView.layoutManager = LinearLayoutManager(inflater.context)

        groupListRecyclerView.adapter = flashcardsGroupsRecyclerViewAdapter

        groupListRecyclerView.adapter = flashcardsGroupsRecyclerViewAdapter

    }

    private fun createRecyclerViewAdapter() = FlashcardsGroupsRecyclerViewAdapter(
        object : OnFlashcardGroupItemClickListener {
            override fun onShortClick(groupId: Long) {
                fragmentViewModel.onEvent(FlashcardGroupListEvent.ShortClickedFlashcardGroup(groupId))
            }

            override fun onLongClick(groupId: Long) {
                fragmentViewModel.onEvent(FlashcardGroupListEvent.LongClickedFlashcardGroup(groupId))
            }
        }
    )

    private fun updateRecyclerViewAdapter(items: List<GroupAdapterItem>)
    {
        flashcardsGroupsRecyclerViewAdapter.saveData(items)
    }

    private fun goToSelectedGroup(groupId: Long)
    {
        val bundle = Bundle()
        bundle.putLong(GROUP_ID_KEY, groupId)
        findNavController().navigate(R.id.action_FragmentFlashcardGroupList_to_FragmentSelectedGroup, bundle)
    }


    fun requestPermissions(){
        permissionHelper.requestPermissions()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}