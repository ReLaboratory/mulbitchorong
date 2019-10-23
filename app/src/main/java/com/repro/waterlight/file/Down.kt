package com.repro.waterlight.file

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.repro.waterlight.R
import kotlinx.android.synthetic.main.activity_down.*
import org.jetbrains.anko.toast

class Down : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_down)
        //상단바 설정
        val ab: androidx.appcompat.app.ActionBar? = supportActionBar
        ab?.setIcon(R.mipmap.logo)
        ab?.setDisplayUseLogoEnabled(true)
        ab?.setDisplayShowHomeEnabled(true)

        var uri: String? = Intent().getStringExtra("a")
        if(uri != null) {
            Glide.with(this).load(uri)
                .into(downImageView)
        }else{
            toast("오류")
            finish()
        }

    }
}
