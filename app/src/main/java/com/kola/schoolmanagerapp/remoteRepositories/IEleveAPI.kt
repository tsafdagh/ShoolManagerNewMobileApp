package com.kola.schoolmanagerapp.remoteRepositories

import com.kola.schoolmanagerapp.entities.ClassRoom
import com.kola.schoolmanagerapp.gestionEleves.Model
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface IEleveAPI {

    @FormUrlEncoded
    @POST("/index.php")
    fun saveStudentPicture(
        @Field("method") methode: String, @Field("matricule") matricule: String, @Field(
            "imageBase64"
        ) pictureBase64: String
    )

    @FormUrlEncoded
    @POST("/index.php")
    fun getStudentByClass(@Field("method") methode: String, @Field("codeClasse") codeClass: String): Observable<ArrayList<Model.Student>>

    @FormUrlEncoded
    @POST("/index.php")
    fun getAllStudents(@Field("method") methode: String): Observable<ArrayList<Model.Student>>

    @FormUrlEncoded
    @POST("/index.php")
    fun getStudent(@Field("method") methode: String, @Field("matricule") matricule: String): Single<Model.Student>

    @FormUrlEncoded
    @POST("/index.php")
    fun getAllClassRoom(@Field("method") methode: String): Observable<List<ClassRoom>>

    @FormUrlEncoded
    @POST("/index.php")
    fun getSchoolInfo(@Field("method") methode: String): Single<Model.SchoolInfo>

}