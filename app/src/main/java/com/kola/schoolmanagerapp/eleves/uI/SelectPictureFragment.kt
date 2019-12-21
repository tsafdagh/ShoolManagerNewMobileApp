package com.kola.schoolmanagerapp.eleves.uI

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.material.snackbar.Snackbar
import com.kola.schoolmanagerapp.R
import kotlinx.android.synthetic.main.select_picture_fragment.*
import org.jetbrains.anko.toast
import java.io.IOException

class SelectPictureFragment : Fragment() {

    companion object {
        fun newInstance() = SelectPictureFragment()
        val REQUEST_PERMISSION_CODE_OPEND_CAMERA = 1
        val REQUEST_PERMISSION_CODE_OPEND_GALLERY = 2

        val CAMERA_REQUEST_CODE = 3
        val GALLERY_REQUEST_CODE = 4
    }

    private lateinit var viewModel: SelectPictureViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.select_picture_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SelectPictureViewModel::class.java)

        arguments?.let { bundle->
            viewModel.student = SelectPictureFragmentArgs.fromBundle(bundle).student
            loadDataToView()
            configureOnClick()
            configureObservers()
            context!!.toast("lecture des données de l'utilisateur courant")

        }

        buttonUpload.setVisibility(View.GONE)
    }

    private fun configureObservers() {
        viewModel.uploadImageObserver.observe(this, Observer {
            it?.let { uploadStatus->
                if(uploadStatus){
                    Snackbar.make(id_selected_student_image, "Image enregidtrer avec succes",Snackbar.LENGTH_LONG).show()
                }else{
                    Snackbar.make(id_selected_student_image, "Erreur d'enregistrement de l'image",Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun loadDataToView(){
        nom_et_prenom_eleve_select_picture.setText("${viewModel.student.nom} ${viewModel.student.prenom}")
        matricule_eleve_select_picture.setText(viewModel.student.matricule)
        Glide.with(this)
            .load(viewModel.student.urlImage)
            .error(R.drawable.user)
            .into(imageView_prview_student)
    }

    private fun configureOnClick() {
        buttonCamera.setOnClickListener {
            opendCamera()
            context!!.toast("Camera bouton click")
        }

        buttonSelectGalerie.setOnClickListener {
            opendGallery()
            context!!.toast("Gallerie bouton click")
        }

        buttonUpload.setOnClickListener {
            uploadData()
        }

        buttonAnuler.setOnClickListener {
            Navigation.findNavController(it).navigateUp()
        }
    }

    private fun uploadData() {
        if(viewModel.imageFixBitmap != null){
            viewModel.sendData()
        }else{
            Snackbar.make(id_selected_student_image,"Veuilez attribuer une image à l'élève", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun opendGallery() {
        if (ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.v("permissions", "Permission is granted")
            val galleryIntent = Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
        } else {

            Log.v("permissions", "Permission is revoked")
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                REQUEST_PERMISSION_CODE_OPEND_GALLERY
            )

        }
    }

    private fun opendCamera() {
        if (ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.v("permissions", "Permission is granted")

            val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)

        } else {

            Log.v("permissions", "Permission is revoked")
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                REQUEST_PERMISSION_CODE_OPEND_CAMERA
            )

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (data != null) {
                context!!.toast("Gallerie result")

                val contentURI = data.data
                try {
                    viewModel.imageFixBitmap =
                        MediaStore.Images.Media.getBitmap(
                            this@SelectPictureFragment.context!!.getContentResolver(),
                            contentURI
                        )
                    // String path = saveImage(bitmap);
                    //Toast.makeText(MainActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    imageView_prview_student.setImageBitmap(viewModel.imageFixBitmap)

                    Glide.with(imageView_prview_student.context)
                        .load(contentURI)
                        .error(R.drawable.user)
                        .into(imageView_prview_student)

                    buttonUpload.setVisibility(View.VISIBLE)
                    //UploadImageOnServerButton.setEnabled(true);
                    Log.v("SELECTPICTUREF", "bitmap from gallerie: ${viewModel.imageFixBitmap.toString()}")

                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(
                        this@SelectPictureFragment.context,
                        "Erreur!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        } else if (requestCode == CAMERA_REQUEST_CODE) {
            context!!.toast("Camera result")

            viewModel.imageFixBitmap = data!!.getExtras()!!.get("data") as Bitmap
            imageView_prview_student.setImageBitmap(viewModel.imageFixBitmap)
            buttonUpload.setVisibility(View.VISIBLE)
            Log.v("SELECTPICTUREF", "bitmap from Camera: ${viewModel.imageFixBitmap.toString()}")
            Glide.with(imageView_prview_student.context)
                .load( data.getExtras()!!.get("data"))
                .error(R.drawable.user)
                .into(imageView_prview_student)

        }
    }

}
