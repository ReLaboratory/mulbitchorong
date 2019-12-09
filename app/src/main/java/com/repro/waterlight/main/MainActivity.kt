package com.repro.waterlight.main

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.repro.waterlight.R
import com.repro.waterlight.home.Home
import com.repro.waterlight.information.Login
import com.repro.waterlight.information.Signup
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.io.InputStream

class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ab: androidx.appcompat.app.ActionBar? = supportActionBar
        ab?.hide()

        val am: AssetManager = resources.assets
        val iss: InputStream = am.open("loadingPhoto.png")
        val bm: Bitmap = BitmapFactory.decodeStream(iss)
        mainLogo.setImageBitmap(bm)
        iss.close()

        //권한 부여 확인
        var permiCheck: Int =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        if (permiCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET),
                REQUEST_CODE
            )
        }

        signUpBtn.setOnClickListener {
            startActivity<Signup>()
        }

        loginBtn.setOnClickListener {
            startActivity<Login>()
            finish()
        }

        GuestTBtn.setOnClickListener {
            startActivity<Home>(
                "check" to false
            )
            finish()
        }
    }

    //요청
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    toast("권한 승인함")
                } else {
                    toast("권한 거부함")
                }
                return
            }
        }
    }
}