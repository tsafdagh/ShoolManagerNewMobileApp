package com.kola.schoolmanagerapp.enseignants

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.kola.schoolmanagerapp.R

class EnseignantFragment : Fragment() {

    companion object {
        fun newInstance() = EnseignantFragment()
    }

    private lateinit var viewModel: EnseignantViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.enseignant_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EnseignantViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
