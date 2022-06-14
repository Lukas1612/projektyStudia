package com.example.readyreadingkotlin.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.readyreadingkotlin.*
import com.example.readyreadingkotlin.learning_unit.LearningUnit


class HomeFragment : Fragment(), IFragmentChanger,  IHomeFragmentDataLoaderListener{

    private val viewModel: HomeViewModel by activityViewModels()

    var learningUnitsList: List<LearningUnit>? = null
    var selectedLearningUnit: LearningUnit? = null

    var smallPassageRecyclerView: RecyclerView? = null
    var SMALL_PASSAGE_TEXT_LENGTH: Int = 200

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false)

        smallPassageRecyclerView = root.findViewById<View>(R.id.smallPassagesRecyclerView) as RecyclerView

        learningUnitsList = viewModel.selectedItem.value


        if(learningUnitsList == null)
        {
            var homeFragmentConnectionDtatLoader = HomeFragmentConnectionDtatLoader(this)
            homeFragmentConnectionDtatLoader.load(root.context)
        }else
        {
            doOnDataLoaded(learningUnitsList!!)
        }


        return root
    }







    fun setTheLUnitFragment() {
        var smallPassagesAdapter: SmallPassagesAdapter = SmallPassagesAdapter(learningUnitsList!!, this, SMALL_PASSAGE_TEXT_LENGTH)

        smallPassageRecyclerView?.adapter = smallPassagesAdapter
        smallPassageRecyclerView?.layoutManager = LinearLayoutManager(context)
    }



    override fun doOnDataLoaded(learningUnits: List<LearningUnit>) {

        clearCurrentLearningUnitsList()

        learningUnitsList = learningUnits
        setTheLUnitFragment()
    }



   override fun changeFragment(fragment: Fragment) {

      saveCurrentLearningUnitsList()

        parentFragmentManager.beginTransaction().apply {
            replace(R.id.nav_host_fragment, fragment)
            commit()
        }
    }

    fun saveCurrentLearningUnitsList()
    {
        viewModel.selectItem(learningUnitsList)
    }

    fun clearCurrentLearningUnitsList()
    {
        viewModel.selectItem(null)
    }

}