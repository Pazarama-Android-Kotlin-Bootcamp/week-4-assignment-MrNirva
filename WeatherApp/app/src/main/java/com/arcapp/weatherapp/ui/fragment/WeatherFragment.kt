package com.arcapp.weatherapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.arcapp.retrofitexample.api.ApiClient
import com.arcapp.weatherapp.R
import com.arcapp.weatherapp.adapter.DailyWeatherAdapter
import com.arcapp.weatherapp.adapter.HourlyWeatherAdapter
import com.arcapp.weatherapp.constant.Constants
import com.arcapp.weatherapp.data.model.weather.WeatherModel
import com.arcapp.weatherapp.data.preference.AppPref
import com.arcapp.weatherapp.data.preference.SharedPref
import com.arcapp.weatherapp.databinding.FragmentWeatherBinding
import com.arcapp.weatherapp.enum.UnitsOfMeasure
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class WeatherFragment : Fragment() {

    // using view binding
    private lateinit var bnd:FragmentWeatherBinding

    private lateinit var navController: NavController

    private lateinit var sharedPref: SharedPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // view binding inflate design
        bnd = FragmentWeatherBinding.inflate(layoutInflater, container, false)
        return bnd.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {

        navController = findNavController()
        sharedPref = SharedPref(requireContext())

        bnd.clLocation.setOnClickListener{
            navController.navigate(R.id.action_weatherFragment_to_searchLocationFragment)
        }

        bnd.ivSettings.setOnClickListener {
            navController.navigate(R.id.action_weatherFragment_to_settingsFragment)
        }

        getPrefData()

    }

    private fun getPrefData(){

        val ap = AppPref(requireContext())

        CoroutineScope(Dispatchers.Main).launch {


            bnd.tvLocationName.text = ap.getLocationName()
            if(ap.getLocationName().isNotEmpty() && ap.getLat() != 0.0 && ap.getLon() != 0.0){
                getWeather(ap.getLat(), ap.getLon())
            }

        }

    }

    private fun getWeather(lat:Double, lon:Double){

        // , Locale.getDefault().language
        ApiClient.getApiService().getWeather(lat, lon, sharedPref.getUnitOfMeasure()).enqueue(object:
            Callback<WeatherModel> {

            override fun onResponse(call: Call<WeatherModel>, response: Response<WeatherModel>) {

                if(response.isSuccessful){

                    Log.e("getWeather","Response: ${response.body()}")

                    val res = response.body()?.current?.weather?.get(0)?.icon

                    Glide.with(requireContext().applicationContext)
                        .load(Constants.IMAGE_API_ADDRESS + res.toString() + Constants.IMAGE_TYPE)
                        .into(bnd.ivWeatherImage)

                    val model = response.body()
                    val current = model?.current
                    val weather = model?.current?.weather?.get(0)

                    val temp = current?.temp?.toInt().toString()+"º"
                    val rain = current?.dewPoint?.toInt().toString() + "%"
                    val humidity = current?.humidity.toString() + "%"
                    val windSpeed = current?.windSpeed?.toInt().toString() + " km/h"
                    val desc = weather?.description?.replaceFirstChar(Char::titlecase)

                    bnd.tvMainTemperature.text = temp
                    bnd.tvMainDesc.text = desc
                    bnd.tvRain.text = rain
                    bnd.tvHumidity.text = humidity
                    bnd.tvWindSpeed.text = windSpeed

                    bnd.rvHourly.adapter = HourlyWeatherAdapter(requireContext(), model?.hourly!!.toMutableList())
                    bnd.rvHourly.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

                    bnd.rvNextForecast.adapter = DailyWeatherAdapter(requireContext(), model.daily!!.toMutableList())
                    bnd.rvNextForecast.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

                }else{
                    Log.e("getWeather","response.code() -> ${response.code()}")
                    Log.e("getWeather","response.code() -> ${response.message()}")
                }

            }

            override fun onFailure(call: Call<WeatherModel>, t: Throwable) {

                t.printStackTrace()
                Log.e("getWeather","onFailure -> ${t.message}")

            }

        })

    }


}