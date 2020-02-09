package com.kola.schoolmanagerapp.gestionEleves

import com.google.gson.annotations.SerializedName
import java.io.Serializable


object Model {
    data class Student(

        @SerializedName("matricule")
        val matricule: String,
        @SerializedName("nom")
        val nom: String,
        @SerializedName("prenom")
        val prenom: String,
        @SerializedName("date")
        val date: String,
        @SerializedName("lieu")
        val lieu: String,
        @SerializedName("sexe")
        val sexe: String,
        @SerializedName("niveau")
        val niveau: String,
        @SerializedName("code_classe")
        val code_classe: String,

        @SerializedName("statu")
        val statu: String,
        @SerializedName("anne_aca")
        val anne_aca: String,
        @SerializedName("image_location")
        val urlImage: String
    ) : Serializable {
        constructor() : this("", "", "", "", "", "", "", "", "", "", "")
    }


    data class SchoolInfo(
        @SerializedName("nom")
        val nom: String,
        @SerializedName("chefEtab")
        val chefEtab: String,
        @SerializedName("devise")
        val devise: String,
        @SerializedName("schooolImageUrl")
        val schooolImageUrl: String
    ) : Serializable
}