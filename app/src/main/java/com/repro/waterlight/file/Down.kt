package com.repro.waterlight.file

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.repro.waterlight.R
import kotlinx.android.synthetic.main.activity_down.*

class Down : AppCompatActivity() {
    var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_down)
        //상단바 설정
        val ab: androidx.appcompat.app.ActionBar? = supportActionBar
        ab?.setIcon(R.mipmap.logo)
        ab?.setDisplayUseLogoEnabled(true)
        ab?.setDisplayShowHomeEnabled(true)

        val intent: Intent = intent
        val surl = intent.getStringExtra("uri")
        uri = Uri.parse(surl)

        Glide.with(this)
            .load(surl)
            .apply(RequestOptions().centerCrop())
            .into(downImageView)

        downButton.setOnClickListener {
            val inten = Intent(Intent.ACTION_VIEW)
            inten.data = uri
            startActivity(inten)
        }
    }
}