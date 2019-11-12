package com.repro.waterlight.main

import android.app.Activity
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.repro.waterlight.R
import com.repro.waterlight.home.Home
import kotlinx.android.synthetic.main.activity_loading.*
import org.jetbrains.anko.startActivity
import java.io.InputStream

class Loading : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        val ab: androidx.appcompat.app.ActionBar? = supportActionBar
        ab?.hide()

        val am: AssetManager = resources.assets
        val iss: InputStream = am.open("loadingPhoto.png")
        val bm: Bitmap = BitmapFactory.decodeStream(iss)
        loading.setImageBitmap(bm)
        iss.close()

        startLoading()
    }

    private fun startLoading() {
        val handler = Handler()
        handler.postDelayed({
            val sign = getSharedPreferences("savesign", Activity.MODE_PRIVATE)
            val sa: Boolean? = sign.getBoolean("key", false)
            val id: String? = sign.getString("id", "")
            if (sa!! && !id.isNullOrEmpty()) {
                 startActivity<Home>(
                     "check" to true,
                     "id" to id
                 )
                finish()
            }else{
                startActivity<MainActivity>()
                finish()
            }

        }, 1000)
    }
}