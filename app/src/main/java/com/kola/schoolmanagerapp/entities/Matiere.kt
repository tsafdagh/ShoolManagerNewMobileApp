package com.kola.schoolmanagerapp.entities

data class Matiere(
    var codeMatiere: String,
    var intitule: String,
    var niveau: String,
    var codeSalle: String,
    var CodeEnseignant: String,
    var coeficient: Double,
    var competance: String,
    var groupeMatiere:String
) {
    constructor() : this("", "", "", "", "", 0.0, "","")
}