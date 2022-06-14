package com.example.readyreadingkotlin.ui.units

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.readyreadingkotlin.R
import com.example.readyreadingkotlin.learning_unit.LearningUnit

class LearningUnitsFragment() : Fragment() {

    var learningUnit: LearningUnit? = null

    private lateinit var viewModel: LearningUnitsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         val root = inflater.inflate(R.layout.learning_units_fragment, container, false)

        var learningUnitsRecyclerView = root.findViewById<View>(R.id.learningUnitsRecyclerView) as RecyclerView

        var theLUnitAdapter = TheLUnitAdapter(learningUnit, 200)

        learningUnitsRecyclerView.adapter = theLUnitAdapter
        learningUnitsRecyclerView?.layoutManager = LinearLayoutManager(context)


        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LearningUnitsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}