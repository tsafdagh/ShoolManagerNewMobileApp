package com.kola.schoolmanagerapp.entities

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.list

@Serializable
data class ClassRoom(
    @SerializedName("nom_salle")
    val code: String,
    @SerializedName("niveau")
    val niveau: String,
    @SerializedName("titulaire")
    val titulaire: String,
    @SerializedName("chef")
    val chef: String
): java.io.Serializable {
    constructor():this("","","","")
}