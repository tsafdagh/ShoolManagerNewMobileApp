package com.kola.schoolmanagerapp.salles_de_classe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kola.schoolmanagerapp.R
import com.kola.schoolmanagerapp.salles_de_classe.items.SalleDeClasseItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder

class SalleDeClasseFragment : Fragment() {

    companion object {
        fun newInstance() = SalleDeClasseFragment()
    }

    private lateinit var viewModel: SalleDeClasseViewModel
    private lateinit var rvSallClass: RecyclerView
    private lateinit var salleDeCasseSection: Section

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.salle_de_classe_fragment, container, false)
        rvSallClass = root.findViewById(R.id.id_rv_salle_class_ragmnt)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SalleDeClasseViewModel::class.java)

        rvSallClass.apply {
            layoutManager = GridLayoutManager(this@SalleDeClasseFragment.context, 2).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            adapter = GroupAdapter<ViewHolder>().apply {
                salleDeCasseSection = Section(getAllSalleDeClasse())
                add(salleDeCasseSection)
                //setOnItemClickListener(onItemClick)
            }
        }
    }

    private fun getAllSalleDeClasse(): ArrayList<SalleDeClasseItem> {
        return ArrayList<SalleDeClasseItem>()
    }
}
