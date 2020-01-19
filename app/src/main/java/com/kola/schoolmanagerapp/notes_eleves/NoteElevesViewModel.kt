package com.kola.schoolmanagerapp.notes_eleves

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

class NoteElevesViewModel(val app:Application) : AndroidViewModel(app) {

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
            EndPoints.URL_ALL_CLASS_ROOM+"&anneAca=${GlobalConfig.ANEEACADEMIQUE}",
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

                            val urlImage =  "http://"+ EndPoints.SERVER_IP.plus("/"+objectstudent.getString("image_location"))
                            Log.d(TAG, "student url image = $urlImage}")

                            val student = Model.Student(
                                objectstudent.getString("matricule"),
                                objectstudent.getString("nom"),
                                objectstudent.getString("prenom"),
                                objectstudent.getString("date") ,
                                objectstudent.getString("lieu"),
                                objectstudent.getString("sexe"),
                                objectstudent.getString("niveau"),
                                objectstudent.getString("code_classe"),
                                objectstudent.getString("statu"),
                                objectstudent.getString("anne_aca"),
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
                params.put("anneAcademique", GlobalConfig.ANEEACADEMIQUE)
                return params
            }
        }

        //adding request to queue
        //VolleySingleton.instance?.addToRequestQueue(stringRequest2)

        val requestQueue = Volley.newRequestQueue(app)
        requestQueue.add<String>(stringRequest2)
    }
}
