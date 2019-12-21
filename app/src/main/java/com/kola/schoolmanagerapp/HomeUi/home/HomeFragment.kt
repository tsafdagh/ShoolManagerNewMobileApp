package com.kola.schoolmanagerapp.HomeUi.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.kola.schoolmanagerapp.R
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    val PICTURE_REQUEST_CODE = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        defineActionBarTheme()
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        configureOnClickListeners()
        checkPermissions()
    }

    private fun checkPermissions() {
        if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.v("permissions", "Permission is granted")

        } else {

            Log.v("permissions", "Permission is revoked")
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                PICTURE_REQUEST_CODE
            )

        }
    }

    private fun configureOnClickListeners() {
        id_card_eleve.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_navigation_home_to_eleveFragment)
        }
        id_card_salle_d_classe.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_navigation_home_to_salleDeClasseFragment)
        }

        id_card_time_table.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_navigation_home_to_emploiDeTempFragment)
        }

        id_card_enseignants.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_navigation_home_to_enseignantFragment)
        }
    }

    private fun defineActionBarTheme() {
        val title = getString(R.string.title_home)
        (activity as AppCompatActivity).supportActionBar!!.apply {
            setBackgroundDrawable(resources.getDrawable(R.drawable.background_white))
            setTitle(HtmlCompat.fromHtml("<font color='#000000'>$title </font>", 0))
            elevation = 0F
            setHomeAsUpIndicator(resources.getDrawable(R.drawable.ic_arrow_back_black_24dp))
        }
    }
}