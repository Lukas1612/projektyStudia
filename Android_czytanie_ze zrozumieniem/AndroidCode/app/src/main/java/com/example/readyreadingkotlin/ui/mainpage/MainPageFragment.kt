package com.example.readyreadingkotlin.ui.mainpage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.example.readyreadingkotlin.R
import com.example.readyreadingkotlin.ui.dashboard.DashboardFragment
import com.example.readyreadingkotlin.ui.home.HomeFragment
import com.example.readyreadingkotlin.ui.notifications.NotificationsFragment

class MainPageFragment() : Fragment() {

    var readingId: CardView? = null
    var statsId: CardView? = null
    var flashcardsId: CardView? = null
    var settingsId: CardView? = null



    private lateinit var viewModel: MainPageViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.main_page_fragment, container, false)

        readingId = view.findViewById(R.id.readingId)
        statsId = view.findViewById(R.id.statsId)
        flashcardsId = view.findViewById(R.id.flashcardsId)
        settingsId = view.findViewById(R.id.settingsId)

        readingId!!.setOnClickListener(View.OnClickListener {
            var homeFragment = HomeFragment()
            setCurrentFragment(homeFragment)
        })

        statsId!!.setOnClickListener(View.OnClickListener {
            setCurrentFragment(NotificationsFragment())
        })

        flashcardsId!!.setOnClickListener(View.OnClickListener {
            setCurrentFragment(DashboardFragment())
        })



        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainPageViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun setCurrentFragment(fragment: Fragment)=
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.nav_host_fragment, fragment)
                commit()
            }


}