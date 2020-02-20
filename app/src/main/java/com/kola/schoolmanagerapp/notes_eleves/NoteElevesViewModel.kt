package com.kola.schoolmanagerapp.notes_eleves

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
import com.kola.schoolmanagerapp.entities.Matiere
import com.kola.schoolmanagerapp.gestionEleves.Model
import com.kola.schoolmanagerapp.notes_eleves.entities.EleveNote
import org.json.JSONException
import org.json.JSONObject

class NoteElevesViewModel(val app: Application) : AndroidViewModel(app) {

    val classRoomListObserver = MutableLiveData<List<ClassRoom>>()
    //val studentClassRoomListObserver = MutableLiveData<ArrayList<Model.Student>>()
    val AllStudentListObserver = MutableLiveData<ArrayList<EleveNote>>()
    private val TAG = "NoteEleveViewModel"

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

                        for (i in 0 until array.length()) {
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

    val saveNoteObserver = MutableLiveData<Boolean>()
    fun saveStudentNote(
        matricule: String,
        codeMatiere: String,
        numSequence: String,
        note: Double,
        examCycle: String = "cc"
    ) {
        val stringRequest = object : StringRequest(
            Request.Method.POST, EndPoints.URL_SAVE_student_NOTES,
            Response.Listener<String> { response ->

                Log.d(TAG, "all String: $response")
                try {
                    val obj = JSONObject(response)

                    val isOk = obj.getBoolean("error")
                    saveNoteObserver.value = isOk

                    val mesage = obj.getString("message")
                    Log.d(TAG, "response String: $mesage")
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Log.d(TAG, "response error: ${e.message}")
                    saveNoteObserver.value = false
                }
            },
            Response.ErrorListener { volleyError ->
                Toast.makeText(app, volleyError.message, Toast.LENGTH_LONG).show()
                Log.d(TAG, "response error: ${volleyError.message}")
                saveNoteObserver.value = false
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("matricule", matricule)
                params.put("code_matiere", codeMatiere)
                params.put("num_sequence", numSequence)
                params.put("notes", "$note")
                params.put("examCycle", examCycle)
                params.put("annee_aca", GlobalConfig.ANEEACADEMIQUE)
                return params
            }
        }

        //adding request to queue
        val requestQueue = Volley.newRequestQueue(app)
        requestQueue.add<String>(stringRequest)
    }


    fun loadStudentsNote(
        classroomId: String,
        codeMatiere: String,
        numSequence: String,
        examCycle: String
    ) {

        val URL = EndPoints.URL_GET_ALL_STUDENTS_NOTE_FOR_CLASSROOM

        //creating volley string request
        val stringRequest2 = object : StringRequest(
            Request.Method.POST, URL,
            Response.Listener<String> { response ->
                try {
                    Log.d(TAG, "response String: $response")

                    val studentList = ArrayList<EleveNote>()

                    val obj = JSONObject(response)
                    if (!obj.getBoolean("error")) {
                        val array = obj.getJSONArray("studentNotes")

                        for (i in 0 until array.length()) {
                            val objectstudent = array.getJSONObject(i)

                            val urlImage =
                                if (!objectstudent.getString("image_location").equals("", true)) {
                                    "http://" + EndPoints.SERVER_IP.plus(
                                        "/" + objectstudent.getString("image_location")
                                    )
                                } else {
                                    "http://" + EndPoints.SERVER_IP.plus(
                                        "APISchoolManager2/students_images/${objectstudent.getString(
                                            "matricule"
                                        )}"
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
                                objectstudent.getString("anne_aca"),
                                urlImage
                            )
                            val studentNote = EleveNote()
                            studentNote.eleve = student
                            studentNote.codeMatiere = objectstudent.getString("code_matiere")
                            studentNote.note = objectstudent.getDouble("note")
                            studentNote.numSequence = objectstudent.getInt("num_sequence")
                            studentList.add(studentNote)
                        }
                        AllStudentListObserver.value = studentList
                    } else {
                        Log.d(TAG, "student error String: ${obj.getString("message")}")
                        AllStudentListObserver.value = studentList
                    }
                } catch (e: JSONException) {
                    Log.d(TAG, "student error String: $e")
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { volleyError ->
                Toast.makeText(app, volleyError.message, Toast.LENGTH_LONG).show()
                Log.d(TAG, "student error String: ${volleyError.message}")
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("code_classe", classroomId)
                params.put("code_matiere", codeMatiere)
                params.put("num_sequence", numSequence)
                params.put("examCycle", examCycle)
                params.put("annee_aca", GlobalConfig.ANEEACADEMIQUE)
                return params
            }
        }

        //adding request to queue
        //VolleySingleton.instance?.addToRequestQueue(stringRequest2)

        val requestQueue = Volley.newRequestQueue(app)
        requestQueue.add<String>(stringRequest2)
    }


    /*
* Cette fonction a pour but de lister l'ensemble
* des sallles de classes de la base de données
* */
    fun loadMatiereParClasses(codeClasse:String, onComplet: (ArrayList<Matiere>) -> Unit, onError:()->Unit) {
        val stringRequest = StringRequest(
            Request.Method.GET,
            EndPoints.URL_MATIERES_DE_LA_CLASSE+ "&codeClasse=$codeClasse",
            Response.Listener<String> { s ->
                try {
                    Log.d(TAG, "response String: $s")
                    val obj = JSONObject(s)
                    if (!obj.getBoolean("error")) {
                        val array = obj.getJSONArray("matieres")

                        val matiereList = ArrayList<Matiere>()

                        for (i in 0 until array.length()) {
                            val objectMatieres = array.getJSONObject(i)
                            val matiere = Matiere()

                            matiere.codeMatiere = objectMatieres.getString("code_matiere")
                            matiere.intitule = objectMatieres.getString("intitule")
                            matiere.niveau = objectMatieres.getString("niveau")
                            matiere.codeSalle = objectMatieres.getString("code_salle")
                            matiere.CodeEnseignant= objectMatieres.getString("enseignant")
                            matiere.coeficient = objectMatieres.getDouble("coeficient")
                            matiere.groupeMatiere = objectMatieres.getString("groupe")
                            matiere.competance = objectMatieres.getString("competance")

                            matiereList.add(matiere)
                        }
                        onComplet(matiereList)
                    } else {
                        Toast.makeText(app, obj.getString("message"), Toast.LENGTH_LONG).show()
                        Log.d(TAG, "matiere error String: ${obj.getString("message")}")
                        onError()

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Log.d(TAG, "matieres error String: ${e}")
                    onError()
                }
            },
            Response.ErrorListener { volleyError ->
                Log.d(TAG, "Error error String: ${volleyError.message}")

                Toast.makeText(
                    app,
                    volleyError.message,
                    Toast.LENGTH_LONG
                ).show()

                onError()
            })


        val requestQueue = Volley.newRequestQueue(app)
        requestQueue.add<String>(stringRequest)
    }



    fun loadStudentsByClassRoom(classroomId: String? = null, onComplet: (ArrayList<Model.Student>) -> Unit) {

        //creating volley string request
        val stringRequest2 = object : StringRequest(
            Request.Method.POST, EndPoints.URL_GET_ALL_STUDENTS_FOR_CLASSROOM,
            Response.Listener<String> { response ->
                try {
                    Log.d(TAG, "response String: $response")

                    val studentList = ArrayList<Model.Student>()
                    val obj = JSONObject(response)
                    if (!obj.getBoolean("error")) {
                        val array = obj.getJSONArray("students")
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
                        onComplet(studentList)
                    } else {
                        Log.d(TAG, "student error String: ${obj.getString("message")}")
                        onComplet(studentList)
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