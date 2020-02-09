package com.kola.schoolmanagerapp.eleves

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.kola.schoolmanagerapp.EndPoints
import com.kola.schoolmanagerapp.GlobalConfig
import com.kola.schoolmanagerapp.entities.ClassRoom
import com.kola.schoolmanagerapp.gestionEleves.Model
import org.json.JSONException
import org.json.JSONObject

class EleveViewModel(private val app: Application) : AndroidViewModel(app) {

    /* private val TAG = "EleveViewModel"
     private val disposable = CompositeDisposable()
     private val eleveService = EleveService()*/

    /*fun getAllClassRoom() {
        disposable.add(
            eleveService.getAllCLassRoom()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<List<ClassRoom>>() {
                    override fun onComplete() {
                        Log.d(TAG, " Chargement de la liste des classes complete ")
                    }

                    override fun onNext(allClassRoom: List<ClassRoom>) {
                        Log.d(TAG, " Liste des salles de classe: $allClassRoom")
                        allClassRoom.forEach {
                            Log.d(TAG, it.toString())
                        }
                        classRoomListObserver.value = allClassRoom
                    }

                    override fun onError(e: Throwable) {
                        Log.d(TAG, "Failed to load classromm list: ${e.toString()}")
                    }

                }
                ))
    }

    fun getAllStudentsByClassRoom(classroomCode: String) {
        disposable.add(
            eleveService.getAllStudentByClass(classroomCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<ArrayList<Model.Student>>() {
                    override fun onComplete() {
                        Log.d(TAG, " Chargement de la liste des élève complete ")
                    }

                    override fun onNext(allStrudentList: ArrayList<Model.Student>) {
                        Log.d(TAG, " Liste des élèves de classe: $allStrudentList")
                        allStrudentList.forEach {
                            Log.d(TAG, it.toString())
                        }
                        studentClassRoomListObserver.value = allStrudentList
                    }

                    override fun onError(e: Throwable) {
                        Log.d(TAG, "Failed to load students classromm list: ${e}")
                    }
                }
                ))
    }

    fun getAllStudents() {
        disposable.add(
            eleveService.getAllStudents()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<ArrayList<Model.Student>>() {
                    override fun onComplete() {
                        Log.d(TAG, " Chargement de la liste des élève complete ")
                    }

                    override fun onNext(allStrudentList: ArrayList<Model.Student>) {
                        Log.d(TAG, " Liste des élèves de classe: $allStrudentList")
                        allStrudentList.forEach {
                            Log.d(TAG, it.toString())
                        }
                        AllStudentListObserver.value = allStrudentList
                    }

                    override fun onError(e: Throwable) {
                        Log.d(TAG, "Failed to load students classromm list: ${e.cause}")
                    }

                }
                ))
    }*/

    val classRoomListObserver = MutableLiveData<List<ClassRoom>>()
    val studentClassRoomListObserver = MutableLiveData<ArrayList<Model.Student>>()
    val AllStudentListObserver = MutableLiveData<ArrayList<Model.Student>>()
    private val TAG = "EleveViewModel"

    /*
    * Cette fonction a pour but de lister l'ensemble
    * des sallles de classes de la base de données
    * */
    fun loadClassRooms() {
        val stringRequest = StringRequest(
            Request.Method.GET,
            EndPoints.URL_ALL_CLASS_ROOM + "&anneAca=${GlobalConfig.ANEEACADEMIQUE}",
            Response.Listener<String> { s ->
                try {
                    Log.d(TAG, "response String: $s")
                    val obj = JSONObject(s)
                    if (!obj.getBoolean("error")) {
                        val array = obj.getJSONArray("classRooms")

                        val classroomList = ArrayList<ClassRoom>()

                        for (i in 0..array.length() - 1) {
                            val objectClassRoom = array.getJSONObject(i)
                            val classroom = ClassRoom(
                                objectClassRoom.getString("nom_salle"),
                                objectClassRoom.getString("niveau"),
                                objectClassRoom.getString("titulaire"),
                                objectClassRoom.getString("chef")
                            )
                            classroomList.add(classroom)
                        }
                        classRoomListObserver.value = classroomList
                    } else {
                        Toast.makeText(app, obj.getString("message"), Toast.LENGTH_LONG).show()
                        Log.d(TAG, "classroom error String: ${obj.getString("message")}")

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Log.d(TAG, "classroom error String: ${e}")
                }
            },
            Response.ErrorListener { volleyError ->
                Log.d(TAG, "classroom error String: ${volleyError.message}")

                Toast.makeText(
                    app,
                    volleyError.message,
                    Toast.LENGTH_LONG
                ).show()
            })


        val requestQueue = Volley.newRequestQueue(app)
        requestQueue.add<String>(stringRequest)
    }

    fun loadStudents(byClassRoom: Boolean = false, classroomId: String? = null) {
        var URL = ""
        if (byClassRoom) {
            URL = EndPoints.URL_GET_ALL_STUDENTS_FOR_CLASSROOM
        } else {
            URL = EndPoints.URL_GET_ALL_STUDENTS
        }

        //creating volley string request
        val stringRequest2 = object : StringRequest(
            Request.Method.POST, URL,
            Response.Listener<String> { response ->
                try {
                    Log.d(TAG, "response String: $response")
                    val obj = JSONObject(response)
                    if (!obj.getBoolean("error")) {
                        val array = obj.getJSONArray("students")

                        val studentList = ArrayList<Model.Student>()

                        for (i in 0..array.length() - 1) {
                            val objectstudent = array.getJSONObject(i)

                            var urlImage = ""
                            if (!objectstudent.getString("image_location").equals("", true)) {
                                urlImage = "http://" + EndPoints.SERVER_IP.plus(
                                    "/" + objectstudent.getString("image_location")
                                )
                            } else {
                                urlImage = "http://" + EndPoints.SERVER_IP.plus(
                                    "APISchoolManager2/students_images/${objectstudent.getString("matricule")}"
                                )
                            }
                            Log.d(TAG, "student url image = $urlImage}")

                            val student = Model.Student(
                                objectstudent.getString("matricule"),
                                objectstudent.getString("nom"),
                                objectstudent.getString("prenom"),
                                objectstudent.getString("date"),
                                objectstudent.getString("lieu"),
                                objectstudent.getString("sexe"),
                                objectstudent.getString("niveau"),
                                objectstudent.getString("code_classe"),
                                objectstudent.getString("statu"),
                                GlobalConfig.ANEEACADEMIQUE,
                                urlImage
                            )
                            studentList.add(student)
                        }
                        AllStudentListObserver.value = studentList
                    } else {
                        Log.d(TAG, "student error String: ${obj.getString("message")}")
                        Toast.makeText(app, obj.getString("message"), Toast.LENGTH_LONG).show()
                    }
                } catch (e: JSONException) {
                    Log.d(TAG, "student error String: ${e}")
                    e.printStackTrace()
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(volleyError: VolleyError) {
                    Toast.makeText(app, volleyError.message, Toast.LENGTH_LONG).show()
                    Log.d(TAG, "student error String: ${volleyError.message}")
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("codeClasse", classroomId ?: "")
                params.put("anneAcademique", GlobalConfig.ANEEACADEMIQUE ?: "")
                return params
            }
        }

        //adding request to queue
        //VolleySingleton.instance?.addToRequestQueue(stringRequest2)

        val requestQueue = Volley.newRequestQueue(app)
        requestQueue.add<String>(stringRequest2)
    }
}
