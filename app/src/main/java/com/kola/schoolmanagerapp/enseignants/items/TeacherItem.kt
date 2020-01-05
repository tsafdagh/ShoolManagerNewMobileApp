package com.kola.schoolmanagerapp.enseignants.items

import com.bumptech.glide.Glide
import com.kola.schoolmanagerapp.R
import com.kola.schoolmanagerapp.enseignants.entities.Teacher
import com.kola.schoolmanagerapp.gestionEleves.Model
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.row_item_teacher.view.*

class TeacherItem(val teacher: Teacher) : Item(){

    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.id_teacherName.text = teacher.nom+" "+teacher.prenom
        viewHolder.itemView.id_teacher_matricule.text = teacher.codeEnseignant
        viewHolder.itemView.id_teacherPhone.text = teacher.telephone

        Glide.with(viewHolder.itemView.context)
            .load(teacher.imageLocation)
            .error(R.drawable.user)
            .circleCrop()
            .into(viewHolder.itemView.id_teacher_image)
    }

    override fun getLayout()= R.layout.row_item_teacher
}