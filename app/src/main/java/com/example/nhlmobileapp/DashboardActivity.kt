package com.example.nhlmobileapp

import android.os.Bundle
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.allViews
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import com.example.nhlmobileapp.databinding.ActivityDashboardBinding
import com.example.nhlmobileapp.responses.WeatherForecastResponse
import com.example.nhlmobileapp.viewmodels.DashboardViewModel
import java.time.format.DateTimeFormatter

class DashboardActivity : AppCompatActivity(), View.OnClickListener {
    private val viewModel: DashboardViewModel by lazy {
        ViewModelProvider(this)[DashboardViewModel::class.java]
    }

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.searchBtn.setOnClickListener(this)
//        renderTable()
    }

    private fun renderTable() {



    }

    private fun handleResponse(response: WeatherForecastResponse) {
//        if (binding.weatherTable.children.count() > 1) {
//            var i = 0
//            for (v: View in binding.weatherTable.children.iterator()) {
//                if (i != 0) {
//                    binding.weatherTable.removeView(v)
//                }
//                i += 1
//            }
//        }

        for (item in response.list) {
            val row = TableRow(this)

            val tv1 = TextView(this)
            val tv2 = TextView(this)
            val tv3 = TextView(this)
            val tv4 = TextView(this)

            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")


            tv1.text  = DateTimeFormatter.ofPattern("HH:mm:ss").format(formatter.parse(item["dt_txt"].toString())).toString()
            tv2.text  = (item["main"] as Map<*, *>)["temp"].toString()
            tv3.text  = (item["main"] as Map<*, *>)["feels_like"].toString()
            tv4.text  = (item["weather"] as ArrayList<Map<*, *>>).get(0)["description"].toString()

            row.addView(tv1)
            row.addView(tv2)
            row.addView(tv3)
            row.addView(tv4)
            binding.weatherTable.addView(row)
        }
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.searchBtn->{
                handleOnSearchClick()
            }
        }
    }

    private fun handleOnSearchClick() {
        viewModel.executeGetCoords(binding.cityField.text.toString())
        viewModel.coordsLiveData.observe(this) { response ->
            if (response.isSuccessful) {
                val coords: Pair<Double, Double>? = response.body.getCoords()

                if (coords == null) {
                    //TODO: Error laten zien
                } else {
                    viewModel.executeGetForecast(coords.first, coords.second)
                    viewModel.forecastLiveData.observe(this) { resp ->
                        if (resp.isSuccessful) {
                            handleResponse(resp.body)
                        }
                    }
                }
            }
        }
    }
}
