package com.kola.schoolmanagerapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.text.HtmlCompat
import com.google.zxing.BarcodeFormat
import kotlinx.android.synthetic.main.activity_app_intro.*
import org.jetbrains.anko.toast

class AppIntroActivity : AppCompatActivity() {


    private val MY_CAMERA_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_intro)
        setScannerProperties()
        defineActionBarTheme()
        startScan()
    }


    private fun setScannerProperties() {
        qrCodeScanner.setFormats(listOf(BarcodeFormat.QR_CODE))
        qrCodeScanner.setAutoFocus(true)
        qrCodeScanner.setLaserColor(R.color.colorAccent)
        qrCodeScanner.setMaskColor(R.color.colorAccent)
        if (Build.MANUFACTURER.equals("HUAWEI", ignoreCase = true))
            qrCodeScanner.setAspectTolerance(0.5f)

    }

    private fun startScan() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.CAMERA),
                    MY_CAMERA_REQUEST_CODE
                )
                return
            }
        }
        qrCodeScanner.startCamera()
        qrCodeScanner.setResultHandler { p0 ->
            if (p0 != null) {
                toast(p0.text)
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }

    override fun onPause() {
        super.onPause()
        qrCodeScanner.stopCamera()
    }

    private fun defineActionBarTheme() {
        val title = getString(R.string.title_scan_qr_code)
        supportActionBar!!.apply {
            setBackgroundDrawable(resources.getDrawable(R.drawable.background_white))
            setTitle(HtmlCompat.fromHtml("<font color='#000000'>$title </font>", 0))
            elevation = 0F
            //setHomeAsUpIndicator(resources.getDrawable(R.drawable.ic_arrow_back_black_24dp))
        }
    }

}
