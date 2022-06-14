package com.example.readyreadingkotlin.back_behaviours

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.readyreadingkotlin.ui.mainpage.MainPageFragment
import com.example.readyreadingkotlin.R
import com.example.readyreadingkotlin.questionSheetsStates.INextBack

class HomeFragmentBehaviour() : INextBack {

    var supportFragmentManager: FragmentManager? = null

    private fun setCurrentFragment(fragment: Fragment)=
            supportFragmentManager!!.beginTransaction().apply {
                replace(R.id.nav_host_fragment, fragment)
                commit()
            }

    override fun next() {

    }

    override fun back() {

        val mainPageFragment = MainPageFragment()
        setCurrentFragment(mainPageFragment)
    }
}