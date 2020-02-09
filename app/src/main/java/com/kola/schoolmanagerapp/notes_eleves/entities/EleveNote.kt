package com.kola.schoolmanagerapp.notes_eleves.entities

import com.kola.schoolmanagerapp.gestionEleves.Model

data class EleveNote(var eleve:Model.Student, var codeMatiere:String, var numSequence:Int, var note:Double) {
    constructor():this(Model.Student(),"",0,0.0)
}