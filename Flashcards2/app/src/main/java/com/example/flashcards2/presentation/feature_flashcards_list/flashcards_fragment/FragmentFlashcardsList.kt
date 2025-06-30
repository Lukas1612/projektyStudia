package com.example.flashcards2.presentation.feature_flashcards_list.flashcards_fragment

import android.os.Bundle
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flashcards2.R
import com.example.flashcards2.databinding.FragmentFlashcardsListBinding
import com.example.flashcards2.presentation.StartActivityHelper
import com.example.flashcards2.presentation.feature_flashcards_list.flashcards_fragment.adapters.FlashcardListAdapterItem
import com.example.flashcards2.presentation.feature_flashcards_list.flashcards_fragment.adapters.FlashcardsRecyclerViewAdapter
import com.example.flashcards2.presentation.feature_flashcards_list.flashcards_fragment.adapters.OnFlashcardClickListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch



@WithFragmentBindings
@AndroidEntryPoint
class FragmentFlashcardsList: Fragment() {

    private var _binding: FragmentFlashcardsListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyListNoticeTextView: TextView
    private lateinit var fab: FloatingActionButton
    private lateinit var flashcardsRecyclerViewAdapter: FlashcardsRecyclerViewAdapter
    private lateinit var toolbar: Toolbar

    private val fragmentViewModel: FlashcardsListViewModel by viewModels()



    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            goBackToFlashcardGroupList()
        }
    }*/
    

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFlashcardsListBinding.inflate(inflater, container, false)

        fab = binding.fab

        toolbar = binding.toolbar
        toolbar.addMenuProvider(createMenuProvider(), viewLifecycleOwner, Lifecycle.State.RESUMED)

        initRecyclerView(inflater)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            fragmentViewModel.state.collectLatest { state ->
                recyclerView.isVisible = state.recyclerViewVisibility
                emptyListNoticeTextView.isVisible =  state.emptyListNoticeVisibility
                toolbar.isVisible = state.isToolBarVisible

                updateRecyclerViewAdapter(state.items)
            }
        }

        lifecycleScope.launch {
            fragmentViewModel.eventFlow.collectLatest { event ->

                when(event){
                    is FlashcardsListViewModel.UiEvent.OpenGroupCreatorScreen -> {
                        StartActivityHelper()
                            .startFlashcardCreatorActivity(
                               activity = activity,
                                flashcardId = event.flashcardId,
                                groupId = event.groupId
                            )
                    }
                }
            }
        }


        fab.setOnClickListener {
            fragmentViewModel.onEvent(FragmentFlashcardsListEvent.FabButtonClicked)
        }

    }



    private fun createMenuProvider(): MenuProvider {
        return object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.fragment_flashcard_groups_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.delete_item -> {
                        fragmentViewModel.onEvent(FragmentFlashcardsListEvent.ClickedDeleteMenuItem)
                        true
                    }

                    R.id.clear_items -> {
                        fragmentViewModel.onEvent(FragmentFlashcardsListEvent.ClickedClearMenuItem)
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun initRecyclerView(inflater: LayoutInflater){
        flashcardsRecyclerViewAdapter = createRecyclerViewAdapter()

        recyclerView = binding.recyclerView
        emptyListNoticeTextView =  binding.emptyListNoticeTextView
        recyclerView.layoutManager = LinearLayoutManager(inflater.context)

        recyclerView.adapter = flashcardsRecyclerViewAdapter
    }

    private fun updateRecyclerViewAdapter(items: List<FlashcardListAdapterItem>){
        flashcardsRecyclerViewAdapter.saveData(items)
    }


    private fun createRecyclerViewAdapter(): FlashcardsRecyclerViewAdapter{
        val adapter = FlashcardsRecyclerViewAdapter(
            object : OnFlashcardClickListener{
                override fun onShortClick(id: Long) {
                    fragmentViewModel.onEvent(FragmentFlashcardsListEvent.ShortClickedFlashcard(id))
                }

                override fun onLongClick(id: Long) {
                    fragmentViewModel.onEvent(FragmentFlashcardsListEvent.LongClickedFlashcard(id))
                }
            }
        )
        return adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}