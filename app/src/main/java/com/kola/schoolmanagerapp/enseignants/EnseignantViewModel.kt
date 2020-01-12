package com.kola.schoolmanagerapp.enseignants

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.kola.schoolmanagerapp.EndPoints
import com.kola.schoolmanagerapp.enseignants.entities.Teacher
import org.json.JSONException
import org.json.JSONObject

class EnseignantViewModel(private val app: Application) : AndroidViewModel(app) {

    val teacherListObserver = MutableLiveData<List<Teacher>>()
    private val TAG = "EnseignantViewModel"
    /*
* Cette fonction a pour but de lister l'ensemble
* des enseignants de l'établissement
* */
    fun loadTeahers() {
        val stringRequest = StringRequest(
            Request.Method.GET,
            EndPoints.URL_ALL_TEACHERS,
            Response.Listener<String> { s ->
                try {
                    Log.d(TAG, "response String: $s")
                    val obj = JSONObject(s)
                    if (!obj.getBoolean("error")) {
                        val array = obj.getJSONArray("teachears")
                        val teacherList = ArrayList<Teacher>()

                        for (i in 0..array.length() - 1) {
                            val objectClassRoom = array.getJSONObject(i)
                            val teacher = Teacher(
                                objectClassRoom.getString("code_enseignant"),
                                objectClassRoom.getString("nom"),
                                objectClassRoom.getString("prenom"),
                                objectClassRoom.getString("telephone"),
                                objectClassRoom.getString("password_ens"),
                                objectClassRoom.getString("image_location")
                            )
                            teacherList.add(teacher)
                        }
                        teacherListObserver.value = teacherList
                    } else {
                        Toast.makeText(app, obj.getString("message"), Toast.LENGTH_LONG).show()
                        Log.d(TAG, "teacher error String: ${obj.getString("message")}")

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Log.d(TAG, "teacher error String: ${e}")
                }
            },
            Response.ErrorListener { volleyError ->
                Log.d(TAG, "teacher error String: ${volleyError.message}")

                Toast.makeText(
                    app,
                    volleyError.message,
                    Toast.LENGTH_LONG
                ).show()
            })

        val requestQueue = Volley.newRequestQueue(app)
        requestQueue.add<String>(stringRequest)
    }
}
