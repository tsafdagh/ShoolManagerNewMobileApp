package com.kola.schoolmanagerapp.notes_eleves

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.kola.schoolmanagerapp.R
import com.kola.schoolmanagerapp.notes_eleves.enums.EnumTypeExam
import kotlinx.android.synthetic.main.layout_ges_note_select_matier_sequence.view.*

object GestionNotesUtils {
    fun showDialoForTypeNoteSelection(
        layoutInflater: LayoutInflater,
        context: Context,
        onListen: (codeMatier: String, typeExem: EnumTypeExam, numeroSequence: Int) -> Unit
    ) {
        val view = layoutInflater.inflate(R.layout.layout_ges_note_select_matier_sequence, null)
        val numberPickeMatiers = view.id_number_picker_code_matiere
        numberPickeMatiers.minValue = 0
        numberPickeMatiers.maxValue = 3
        numberPickeMatiers.displayedValues = arrayOf("ENGL", "MATH", "GEO", "ECM", "INFO")

        AlertDialog.Builder(context)
            .setTitle("Choose period")
            .setView(view)
            .setNegativeButton(context.getString(R.string.cancel), null)
            .setPositiveButton(context.getString(R.string.ok)) { dialogInterface, i ->

                onListen("ENG",EnumTypeExam.CC, 1)

            }

            .create()
            .show()
    }
}