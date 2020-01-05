package com.kola.schoolmanagerapp.enseignants

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kola.schoolmanagerapp.R
import com.kola.schoolmanagerapp.enseignants.entities.Teacher
import com.kola.schoolmanagerapp.enseignants.items.TeacherItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import org.jetbrains.anko.toast

class EnseignantFragment : Fragment() {

    companion object {
        fun newInstance() = EnseignantFragment()
    }

    private lateinit var viewModel: EnseignantViewModel

    private lateinit var rvEnseignants: RecyclerView
    private lateinit var enseignantSection: Section

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.enseignant_fragment, container, false)
        rvEnseignants = root.findViewById(R.id.id_rv_enseignant_fragmnt)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(EnseignantViewModel::class.java)
        viewModel.loadTeahers()

        configureObservers()
    }

    @SuppressLint("WrongConstant")
    private fun configureObservers() {
        viewModel.teacherListObserver.observe(this, Observer { listOfTeachers ->
            rvEnseignants.apply {
                layoutManager = LinearLayoutManager(this@EnseignantFragment.context, LinearLayout.HORIZONTAL,false).apply {
                    orientation = LinearLayoutManager.HORIZONTAL
                }
                adapter = GroupAdapter<ViewHolder>().apply {
                    enseignantSection = Section(convertClassListToItem(listOfTeachers))
                    add(enseignantSection)
                    setOnItemClickListener { item, view ->

                        if (item is TeacherItem) {
                            context!!.toast("OnClick item")
                        }

                    }
                }
            }
        })
    }

    private fun convertClassListToItem(listOfTeachers: List<Teacher>?): ArrayList<TeacherItem> {
        val itemsList = ArrayList<TeacherItem>()
        if (listOfTeachers != null) {
            listOfTeachers.forEach {
                itemsList.add(TeacherItem(it))
            }
        }
        return itemsList
    }

}
