package com.arcapp.weatherapp.ui.fragment

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.arcapp.weatherapp.R
import com.arcapp.weatherapp.data.preference.AppPref
import com.arcapp.weatherapp.databinding.FragmentSplashBinding
import com.arcapp.weatherapp.enum.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    // using view binding
    private lateinit var bnd:FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // view binding inflate design
        bnd = FragmentSplashBinding.inflate(layoutInflater, container, false)
        return bnd.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {

        object : CountDownTimer(2500, 1000){

            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                checkData()
            }

        }.start()

    }

    private fun checkData(){

        view?.post {

            val ap = AppPref(requireContext())

            CoroutineScope(Dispatchers.Main).launch {

                if(ap.getLocationName().isNotEmpty() && ap.getLat() != 0.0 && ap.getLon() != 0.0){

                    findNavController().navigate(R.id.action_splashFragment_to_weatherFragment)

                }else{

                    findNavController().navigate(R.id.action_splashFragment_to_searchLocationFragment)

                }

            }

        }

    }

}