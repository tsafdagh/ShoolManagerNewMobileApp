package com.kola.schoolmanagerapp.eleves.items

import com.bumptech.glide.Glide
import com.kola.schoolmanagerapp.R
import com.kola.schoolmanagerapp.gestionEleves.Model
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.row_item_eleve.view.*

class EleveItem(val eleve: Model.Student) : Item(){

    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.id_row_item_student_name.text = eleve.nom+" "+eleve.prenom
        viewHolder.itemView.id_row_item_student_matricule.text = eleve.matricule
        viewHolder.itemView.id_row_item_student_sex.text = eleve.sexe
        viewHolder.itemView.id_row_item_student_salle_classe.text = "Classe: ${eleve.code_classe}"

        Glide.with(viewHolder.itemView.context)
            .load(eleve.urlImage)
            .error(R.drawable.user)
            .circleCrop()
            .into(viewHolder.itemView.id_row_item_student_image)
    }

    override fun getLayout()= R.layout.row_item_eleve
}