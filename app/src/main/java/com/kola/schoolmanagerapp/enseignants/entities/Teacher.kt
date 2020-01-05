package com.kola.schoolmanagerapp.enseignants.entities

data class Teacher(
    val codeEnseignant: String,
    val nom: String,
    val prenom: String,
    val telephone: String,
    val passwordEns: String,
    val imageLocation: String
) {
    constructor() : this("", "", "", "", "", "")
}