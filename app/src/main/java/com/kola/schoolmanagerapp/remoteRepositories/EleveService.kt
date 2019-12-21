package com.kola.schoolmanagerapp.remoteRepositories

import com.google.gson.GsonBuilder
import com.kola.schoolmanagerapp.entities.ClassRoom
import com.kola.schoolmanagerapp.gestionEleves.Model
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class EleveService {

    companion object {
        var serverIP = "192.168.43.11"
    }

    val baseUrl = "http://$serverIP/APISchoolManager/public/"

    //https://www.javahelps.com/2018/12/access-mysql-from-android-through.html

    //https://www.simplifiedcoding.net/retrofit-android-tutorial/

    //https://stackoverflow.com/questions/40176945/how-to-call-php-using-retrofit-in-android

    //https://www.sitepoint.com/retrofit-a-simple-http-client-for-android-and-java/

    //https://www.simplifiedcoding.net/volley-with-kotlin/

    val gson = GsonBuilder()
        .setLenient()
        .create()

    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // on ajoute ceci pour dire à retrofit que nous allons utiliser RXjava
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val service = retrofit.create(IEleveAPI::class.java)


    fun getAllCLassRoom(): Observable<List<ClassRoom>> {
        return service.getAllClassRoom("getAllClassRoom")
    }

    fun getAllStudents(): Observable<ArrayList<Model.Student>> {
        return service.getAllStudents("getAllStudents")
    }


    fun getAllStudentByClass(codeClass: String): Observable<ArrayList<Model.Student>> {
        return service.getStudentByClass("getStudentByClass", codeClass)
    }

    fun getStudent(matricule: String): Single<Model.Student> {
        return service.getStudent("getStudent", matricule)
    }

    fun getSchoolInfo(): Single<Model.SchoolInfo> {
        return service.getSchoolInfo("getSchoolInfo")
    }
}