package com.kola.schoolmanagerapp.eleves.uI

import android.app.Application
import android.graphics.Bitmap
import android.util.Base64
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
import com.kola.schoolmanagerapp.VolleySingleton
import com.kola.schoolmanagerapp.gestionEleves.Model
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class SelectPictureViewModel(private val app: Application) : AndroidViewModel(app) {

    val TAG ="SelectPictureVM"
     var imageFixBitmap: Bitmap?= null

    lateinit var  student:Model.Student

    val uploadImageObserver = MutableLiveData<Boolean>()

    private fun compressImageToByteArray(): String {
        val byteOutputStream = ByteArrayOutputStream()
        var byteArray: ByteArray
        imageFixBitmap!!.compress(Bitmap.CompressFormat.JPEG, 40, byteOutputStream)
        byteArray = byteOutputStream.toByteArray()
        val convertImage = Base64.encodeToString(byteArray, Base64.DEFAULT)
        return convertImage
    }

    fun sendData(){
        //creating volley string request
        val stringRequest = object : StringRequest(
            Request.Method.POST, EndPoints.URL_ADD_STUDENT_PICTURE,
            Response.Listener<String> { response ->

                Log.d(TAG, "all String: $response")
                try {
                    val obj = JSONObject(response)
                    val mesage =  obj.getString("message")
                    Log.d(TAG, "response String: $mesage")
                    //TODO controller si la reposse est positive
                    uploadImageObserver.value = true
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Log.d(TAG, "response error: ${e.message}")
                    uploadImageObserver.value = false
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(volleyError: VolleyError) {
                    Toast.makeText(app, volleyError.message, Toast.LENGTH_LONG).show()
                    Log.d(TAG, "response error: ${volleyError.message}")
                    uploadImageObserver.value = false
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("matricule", student.matricule)
                params.put("imageBase64", compressImageToByteArray())
                params.put("codeClasse", student.code_classe)
                return params
            }
        }

        //adding request to queue
        val requestQueue = Volley.newRequestQueue(app)
        requestQueue.add<String>(stringRequest)
    }
}
