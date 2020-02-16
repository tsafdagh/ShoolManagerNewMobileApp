package com.kola.schoolmanagerapp.notes_eleves

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kola.schoolmanagerapp.R
import com.kola.schoolmanagerapp.eleves.items.EleveItem
import com.kola.schoolmanagerapp.entities.ClassRoom
import com.kola.schoolmanagerapp.gestionEleves.Model
import com.kola.schoolmanagerapp.notes_eleves.entities.EleveNote
import com.kola.schoolmanagerapp.notes_eleves.enums.EnumTypeExam
import com.kola.schoolmanagerapp.notes_eleves.items.EleveNoteItem
import com.kola.schoolmanagerapp.notes_eleves.items.SalleDeClasseItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.botomsheet_note.*
import kotlinx.android.synthetic.main.botomsheet_note.view.*
import kotlinx.android.synthetic.main.note_eleve_fragment.*
import org.jetbrains.anko.toast

class NoteElevesFragment : Fragment() {


    companion object {
        fun newInstance() = NoteElevesFragment()
        lateinit var eleveSelectionner: Model.Student
        lateinit var codeMatiereSelectionner: String
        lateinit var numSequenceSelectionner: String
        lateinit var cycleEvalSelectionner: String

        private val TAG = "NoteElevesFragment"
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
        defineActionBarTheme()
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(NoteElevesViewModel::class.java)
        configureObservers()

        viewModel.loadClassRooms()
        // viewModel.loadStudents(false, "")

        configureOnclick()

        codeMatiereSelectionner = "INFO-4e Esp"
        numSequenceSelectionner = "1"
        cycleEvalSelectionner = "cc"


        viewModel.loadStudentsNote(
            "4e Esp",
            codeMatiereSelectionner,
            numSequenceSelectionner,
            cycleEvalSelectionner
        )
    }

    private fun configureOnclick() {

        val cycleEvalArray = resources.getStringArray(R.array.cycle_evaluation)
        val numeroSequenceArray = resources.getStringArray(R.array.numero_sequence)

        id_spinner_cycle.onItemSelectedListener = object : AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemClick(
                parent: AdapterView<*>,
                view: View,
                pos: Int,
                id: Long
            ) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                pos: Int,
                id: Long
            ) {

                if (cycleEvalArray[pos].equals(1)) {
                    cycleEvalSelectionner = "dh"
                } else {
                    cycleEvalSelectionner = cycleEvalArray[pos]
                }
                Log.d(TAG, "Cycle évaluation selectionner: $cycleEvalSelectionner")
            }

        }

        id_spinner_sequences.onItemSelectedListener = object : AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemClick(
                parent: AdapterView<*>,
                view: View,
                pos: Int,
                id: Long
            ) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                pos: Int,
                id: Long
            ) {
                numSequenceSelectionner = numeroSequenceArray[pos]
                Log.d(TAG, "numero sequence selectionner: $numSequenceSelectionner")
            }

        }


        id_edt_gestion_note_note_eleve.setOnClickListener {

        }

        id_btn_gestion_note_enregistrer.setOnClickListener {
            if (controleNote()) {
                val noteEleve = id_edt_gestion_note_note_eleve.text.toString().toDouble()

                codeMatiereSelectionner = "ANG-2ndC"

                viewModel.saveStudentNote(
                    eleveSelectionner.matricule,
                    codeMatiereSelectionner,
                    numSequenceSelectionner,
                    noteEleve,
                    cycleEvalSelectionner
                )
            }
        }

    }

    private fun controleNote(): Boolean {

        try {
            val noteString = id_edt_gestion_note_note_eleve.text.toString()

            val noteDouble = noteString.toDouble()
            if (noteDouble in 0.0..20.0) {
                return true
            } else {
                id_edt_gestion_note_note_eleve.error =
                    getString(R.string.erreur_champ_note_deborder)
                return false
            }

        } catch (exception: Exception) {
            id_edt_gestion_note_note_eleve.error = getString(R.string.erreur_note_non_valide)
            return false
        }

    }

    private fun configureObservers() {
        viewModel.classRoomListObserver.observe(this, Observer { listOfClass ->
            rvSallClass.apply {
                layoutManager = LinearLayoutManager(
                    this@NoteElevesFragment.context,
                    LinearLayout.HORIZONTAL,
                    false
                ).apply {
                    orientation = LinearLayoutManager.HORIZONTAL
                }
                adapter = GroupAdapter<ViewHolder>().apply {
                    salleDeCasseSection = Section(convertClassListToItem(listOfClass))
                    add(salleDeCasseSection)
                    setOnItemClickListener { item, view ->

                        if (item is SalleDeClasseItem) {
                            context!!.toast("OnClick item")
                            //loadStudentsForClass(item.classRoom.code)
                            //viewModel.loadStudents(true, item.classRoom.code)

                            viewModel.loadMatiereParClasses(
                                item.classRoom.code,
                                onComplet = { matiereList ->
                                    matiereList.forEach {
                                        Log.d(TAG, it.toString())
                                    }
                                },
                                onError = {
                                    Log.d(TAG, "Error to load data")
                                })

                            GestionNotesUtils.showDialoForTypeNoteSelection(layoutInflater,context,onListen = {
                                codeMatier:String,typeExam:EnumTypeExam,numSeq:Int ->

                                viewModel.loadStudentsNote(
                                    item.classRoom.code,
                                    codeMatiereSelectionner,
                                    numSequenceSelectionner,
                                    cycleEvalSelectionner
                                )
                            })


                        }

                    }
                }
            }
        })
        viewModel.AllStudentListObserver.observe(this, Observer { studentList ->
            showStudentListInView(studentList)
        })

        viewModel.saveNoteObserver.observe(this, Observer {
            it?.let { noteSaved ->
                if (noteSaved) {
                    context!!.toast("Note enregistrer avec succes")
                } else {
                    context!!.toast("Erreur d'enregistrement de la note")
                }
            }
        })
    }


    /**
     * Au click sur une salle de classe, cette fonction charge tous les élèves
     * de la dite salle de classe
     */
    private fun showStudentListInView(studentList: ArrayList<EleveNote>) {
        rvEleves.apply {
            layoutManager = LinearLayoutManager(
                this@NoteElevesFragment.context,
                LinearLayout.VERTICAL,
                false
            ).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            adapter = GroupAdapter<ViewHolder>().apply {
                elleveSection = Section(convertStudentListToItem(studentList))
                add(elleveSection)
                setOnItemClickListener { item, view ->

                    if (item is EleveNoteItem) {
                        if (selectedBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                            selectedBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED)
                            eleveSelectionner = item.eleveNote.eleve
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

    private fun convertStudentListToItem(studentList: List<EleveNote>): ArrayList<EleveNoteItem> {
        val itemsList = ArrayList<EleveNoteItem>()
        studentList.forEach {
            itemsList.add(EleveNoteItem(it))
        }
        return itemsList
    }

    private fun defineActionBarTheme() {
        val title = getString(R.string.Eregistrement_note)
        (activity as AppCompatActivity).supportActionBar!!.apply {
            setBackgroundDrawable(ColorDrawable(Color.BLACK))
            //setTitle(HtmlCompat.fromHtml("<font color=#4F5F8E>$title</font>", 0))
            setTitle(title)
            elevation = 0F
            setHomeAsUpIndicator(resources.getDrawable(R.drawable.ic_arrow_back_white24dp))
            setHasOptionsMenu(false)
        }
    }
}
