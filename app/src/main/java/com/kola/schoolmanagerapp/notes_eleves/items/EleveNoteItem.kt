package com.kola.schoolmanagerapp.notes_eleves.items

import android.graphics.Color
import com.bumptech.glide.Glide
import com.kola.schoolmanagerapp.R
import com.kola.schoolmanagerapp.notes_eleves.entities.EleveNote
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.row_item_eleve_note.view.*
import org.jetbrains.anko.textColor

class EleveNoteItem(val eleveNote: EleveNote) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.id_row_itemNote_student_name.text =
            eleveNote.eleve.nom + " " + eleveNote.eleve.prenom
        viewHolder.itemView.id_row_itemNote_student_matricule.text = eleveNote.eleve.matricule
        viewHolder.itemView.id_row_itemNote_student_sex.text = eleveNote.eleve.sexe
        viewHolder.itemView.id_row_itemNote_student_salle_classe.text =
            "Classe: ${eleveNote.eleve.code_classe}"

        viewHolder.itemView.id_row_itemNote_Note_eleve.text = eleveNote.note.toString()
        viewHolder.itemView.id_row_itemNote_code_matiere.text = eleveNote.codeMatiere

        if (eleveNote.note > 10) {
            viewHolder.itemView.id_row_itemNote_Note_eleve.textColor = Color.GREEN
        } else {
            viewHolder.itemView.id_row_itemNote_Note_eleve.textColor = Color.RED
        }

        Glide.with(viewHolder.itemView.context)
            .load(eleveNote.eleve.urlImage)
            .error(R.drawable.user)
            .circleCrop()
            .into(viewHolder.itemView.id_row_itemNote_student_image)
    }

    override fun getLayout() = R.layout.row_item_eleve_note
}