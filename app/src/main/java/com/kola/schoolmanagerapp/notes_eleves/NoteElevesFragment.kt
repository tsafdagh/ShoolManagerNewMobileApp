package com.kola.schoolmanagerapp.notes_eleves

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kola.schoolmanagerapp.R
import com.kola.schoolmanagerapp.eleves.items.EleveItem
import com.kola.schoolmanagerapp.entities.ClassRoom
import com.kola.schoolmanagerapp.gestionEleves.Model
import com.kola.schoolmanagerapp.notes_eleves.items.SalleDeClasseItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.botomsheet_note.view.*
import org.jetbrains.anko.toast

class NoteElevesFragment : Fragment() {


    companion object {
        fun newInstance() = NoteElevesFragment()
    }

    private lateinit var viewModel: NoteElevesViewModel


    private lateinit var rvSallClass: RecyclerView
    private lateinit var salleDeCasseSection: Section

    private lateinit var rvEleves: RecyclerView
    private lateinit var elleveSection: Section

    private lateinit var selectBottomSheet: LinearLayout
    lateinit var selectedBottomSheetBehavior: BottomSheetBehavior<*>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.note_eleve_fragment, container, false)
        rvSallClass = root.findViewById(R.id.id_rv_select_student_classrom_note)
        rvEleves = root.findViewById(R.id.id_rv_all_students_note)
        selectBottomSheet = root.note_bottomsheet
        selectedBottomSheetBehavior = BottomSheetBehavior.from(selectBottomSheet)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(NoteElevesViewModel::class.java)
        configureObservers()

        viewModel.loadClassRooms()
        viewModel.loadStudents(false, "")

    }

    private fun configureObservers() {
        viewModel.classRoomListObserver.observe(this, Observer { listOfClass ->
            rvSallClass.apply {
                layoutManager = LinearLayoutManager(this@NoteElevesFragment.context, LinearLayout.HORIZONTAL,false).apply {
                    orientation = LinearLayoutManager.HORIZONTAL
                }
                adapter = GroupAdapter<ViewHolder>().apply {
                    salleDeCasseSection = Section(convertClassListToItem(listOfClass))
                    add(salleDeCasseSection)
                    setOnItemClickListener { item, view ->

                        if (item is SalleDeClasseItem) {
                            context!!.toast("OnClick item")
                            //loadStudentsForClass(item.classRoom.code)
                            viewModel.loadStudents(true, item.classRoom.code)

                        }

                    }
                }
            }
        })
        viewModel.AllStudentListObserver.observe(this, Observer { studentList ->
            showStudentListInView(studentList)
        })
    }


    /**
     * Au click sur une salle de classe, cette fonction charge tous les élèves
     * de la dite salle de classe
     */
    private fun showStudentListInView(studentList: ArrayList<Model.Student>) {
        rvEleves.apply {
            layoutManager = LinearLayoutManager(this@NoteElevesFragment.context, LinearLayout.VERTICAL,false).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            adapter = GroupAdapter<ViewHolder>().apply {
                elleveSection = Section(convertStudentListToItem(studentList))
                add(elleveSection)
                setOnItemClickListener { item, view ->

                    if (item is EleveItem) {
                        if (selectedBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                            selectedBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED)
                        } else {
                            selectedBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
                            //context!!.toast("Expand sheet");
                        }
                    }
                }
            }
        }
    }


    /**
     * Cette fonction a pour but de lire l'ensemble des sallles de
     * classes dans le serveur
     */
    private fun getEleveForSalleDeClasse(code: String): ArrayList<EleveItem> {
        return ArrayList<EleveItem>()
    }

    /**
     * Cette fonction a pour but de lire l'ensemble des
     * élèves d'une salle de classe dans le serveur
     */
    private fun convertClassListToItem(listOfClass: List<ClassRoom>): ArrayList<SalleDeClasseItem> {
        val itemsList = ArrayList<SalleDeClasseItem>()
        listOfClass.forEach {
            itemsList.add(SalleDeClasseItem(it))
        }
        return itemsList
    }

    private fun convertStudentListToItem(studentList: List<Model.Student>): ArrayList<EleveItem> {
        val itemsList = ArrayList<EleveItem>()
        studentList.forEach {
            itemsList.add(EleveItem(it))
        }
        return itemsList
    }
}
