package com.example.readyreadingkotlin.ui.notifications

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.readyreadingkotlin.R
import com.example.readyreadingkotlin.auth.ApiClient
import com.example.readyreadingkotlin.auth.LoginActivity
import com.example.readyreadingkotlin.auth.SessionManager
import com.example.readyreadingkotlin.learning_unit.UserTests
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private lateinit var lineChart: LineChart
    private var scoreList = ArrayList<UserTests>()

    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
                ViewModelProvider(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)

        apiClient = ApiClient()
        sessionManager = SessionManager(root.context)

        lineChart = root.findViewById(R.id.lineChart)

        val intent = Intent(getActivity(), LoginActivity::class.java)
        startActivity(intent)


        if(sessionManager.fetchAuthToken() != null && sessionManager.fetchAuthToken()!!.length>5)
        {
            println(" flashcards:  ")

            setDataToLineChart()
            initLineChart()
        }


        return root
    }


    inner class MyAxisFormatter : IndexAxisValueFormatter() {

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            return if (index < scoreList.size) {
                scoreList[index].date.toString()
            } else {
                ""
            }
        }
    }

    private fun setDataToLineChart() {

        val entries: ArrayList<BarEntry> = ArrayList()

        // Pass the token as parameter
        apiClient.getApiService().getUserTest(token = "Bearer ${sessionManager.fetchAuthToken()}")
                .enqueue(object : Callback<List<UserTests>> {
                    override fun onFailure(call: Call<List<UserTests>>, t: Throwable) {
                        // Error fetching posts
                        println(" error " + t.toString() + "  " + call.toString())
                    }

                    override fun onResponse(
                            call: Call<List<UserTests>>,
                            response: Response<List<UserTests>>
                    ) {

                        println(" --- " + response.body())
                        if (response != null && response.body() != null && response!!.body()!!.size > 0) {
                            scoreList = response.body() as ArrayList<UserTests>

                            //now draw bar chart with dynamic data
                            val entries: ArrayList<Entry> = ArrayList()


                            //you can replace this data object with  your custom object
                            for (i in scoreList.indices) {
                                val score = scoreList[i]
                                entries.add(Entry(i.toFloat(), score.score!!.toFloat()))
                            }

                            var lineDataSet = LineDataSet(entries, "")
                            lineDataSet.setLineWidth(3f)
                            lineDataSet.setCircleRadius(6f)
                            lineDataSet.setValueTextSize(10f)

                            val data = LineData(lineDataSet)
                            lineChart.data = data

                            lineChart.invalidate()
                        }

                    }
                })
    }

    private fun initLineChart() {
//        hide grid lines
        lineChart.axisLeft.setDrawGridLines(true)
        val xAxis: XAxis = lineChart.xAxis
        xAxis.setDrawGridLines(true)
        xAxis.setDrawAxisLine(true)

        //remove right y-axis
        lineChart.axisRight.isEnabled = true

        //remove legend
        lineChart.legend.isEnabled = true


        //remove description label
        lineChart.description.isEnabled = true


        //add animation
        lineChart.animateX(1000, Easing.EaseInSine)

        // to draw label on xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
        xAxis.valueFormatter = MyAxisFormatter()
        xAxis.setDrawLabels(true)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = +90f
        xAxis.enableGridDashedLine(10f, 10f, 0f);
    }

}