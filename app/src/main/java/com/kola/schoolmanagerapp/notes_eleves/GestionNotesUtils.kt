package com.kola.schoolmanagerapp.notes_eleves

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import com.kola.schoolmanagerapp.R
import com.kola.schoolmanagerapp.notes_eleves.enums.EnumTypeExam
import kotlinx.android.synthetic.main.layout_ges_note_select_matier_sequence.view.*

object GestionNotesUtils {

    val TAG = "GestionNotesUtils"

    fun showDialoForTypeNoteSelection(
        layoutInflater: LayoutInflater,
        context: Context,
        listeCodeMatiere: ArrayList<String>,
        onListen: (codeMatier: String, typeExem: EnumTypeExam, numeroSequence: Int) -> Unit
    ) {
        val view = layoutInflater.inflate(R.layout.layout_ges_note_select_matier_sequence, null)
        val numberPickeMatiers = view.id_number_picker_code_matiere
        numberPickeMatiers.minValue = 0
        numberPickeMatiers.maxValue = listeCodeMatiere.size - 1

        numberPickeMatiers.setFormatter {
            return@setFormatter listeCodeMatiere[it]
        }

        AlertDialog.Builder(context)
            .setTitle("Choose period")
            .setView(view)
            .setNegativeButton(context.getString(R.string.cancel), null)
            .setPositiveButton(context.getString(R.string.ok)) { dialogInterface, i ->

                val codeMatiereSelection = view.id_number_picker_code_matiere.value
                val numeroSequence = view.id_spinner_numero_sequence.selectedItem.toString().toInt()
                var cycleExam: EnumTypeExam

                Log.d(TAG, "Code matiere numberPicker : $codeMatiereSelection")
                Log.d(TAG, "Code matiere de la liste : ${listeCodeMatiere[codeMatiereSelection]}")
                Log.d(TAG, "Numero sequence: $numeroSequence")

                when (view.id_radio_button_cycle_exam.checkedRadioButtonId) {
                    R.id.id_radio_cycle_exam_cc -> {
                        cycleExam = EnumTypeExam.CC
                    }
                    R.id.id_radio_cycle_exam_ds -> {
                        cycleExam = EnumTypeExam.DS
                    }

                    R.id.id_rv_all_students_note -> {
                        cycleExam = EnumTypeExam.NOTE
                    }
                    else -> {
                        cycleExam = EnumTypeExam.CC
                    }
                }

                Log.d(TAG, "cycle exam: $cycleExam")
                onListen(listeCodeMatiere[codeMatiereSelection], cycleExam, numeroSequence)

            }

            .create()
            .show()
    }
}