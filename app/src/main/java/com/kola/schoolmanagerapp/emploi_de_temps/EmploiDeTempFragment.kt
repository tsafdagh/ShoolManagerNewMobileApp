package com.kola.schoolmanagerapp.emploi_de_temps

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.kola.schoolmanagerapp.R

class EmploiDeTempFragment : Fragment() {

    companion object {
        fun newInstance() = EmploiDeTempFragment()
    }

    private lateinit var viewModel: EmploiDeTempViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.emploi_de_temp_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EmploiDeTempViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
