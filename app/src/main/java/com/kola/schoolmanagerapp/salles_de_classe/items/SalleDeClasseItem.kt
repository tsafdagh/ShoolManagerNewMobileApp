package com.kola.schoolmanagerapp.salles_de_classe.items

import com.kola.schoolmanagerapp.R
import com.kola.schoolmanagerapp.entities.ClassRoom
import com.kola.schoolmanagerapp.gestionEleves.Model
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.row_item_salle_de_classe.view.*

class SalleDeClasseItem(val classRoom: ClassRoom) : Item(){

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.id_b_tv_itt_classe_code.text = classRoom.code
        viewHolder.itemView.id_b_tv_nom_salle.text = classRoom.niveau
    }

    override fun getLayout()= R.layout.row_item_salle_de_classe
}