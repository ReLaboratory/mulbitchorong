package com.repro.waterlight.file

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.repro.waterlight.R
import kotlinx.android.synthetic.main.activity_down.*
import java.io.File
import java.io.FileOutputStream

class Down : AppCompatActivity() {
    var dow: Bitmap? = null
    var name: String? = null
    var id: String? = null

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_down)
        //상단바 설정
        val ab: androidx.appcompat.app.ActionBar? = supportActionBar
        ab?.setIcon(R.mipmap.logo)
        ab?.setDisplayUseLogoEnabled(true)
        ab?.setDisplayShowHomeEnabled(true)

        val bytes = intent.getByteArrayExtra("file")
        dow = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        name = intent.getStringExtra("name")
        id = intent.getStringExtra("id")

        Glide.with(this)
            .load(dow)
            .apply(RequestOptions().centerCrop())
            .into(downImageView)

        downButton.setOnClickListener {
            saveBitmaptoJpeg()
//            startActivity<Home>(
//                "check" to true,
//                "id" to id
//            )
//            finish()
        }
    }

    fun saveBitmaptoJpeg() {
        val ex_storage: String = Environment.getExternalStorageDirectory().absolutePath
        val filePath =
            File("$ex_storage/mulbit/$name")
            //File("/sdcard/Download/mulbit/$name")
        if (!filePath.isDirectory){
            filePath.mkdirs()
            Log.e("ffffff","폴더 생성 성공")
        }

        if(filePath.exists()) {
            val out =
                FileOutputStream("$ex_storage/mulbit/$name.jpg")
                //FileOutputStream("/sdcard/Download/mulbit/$name.jpg")
            dow?.compress(Bitmap.CompressFormat.JPEG, 2, out)
            out.close()

            Log.e("ffffff", filePath.parent)
            Log.e("ffffff", filePath.path)
            Log.e("ffffff", filePath.name)
            Log.e("ffffff", filePath.toString())
            Log.e("ffffff", filePath.exists().toString())
            Log.e("ffffff", filePath.absolutePath)
            Log.e("ffffff","성공")
        }else{
            Log.e("ffffff","실패")
        }

        ///////////////////////////////////////////////////////////

//        val root = Environment.getExternalStorageDirectory().toString()
//        val myDir = File("$root/req_images")
//        myDir.mkdirs()
//        val generator = Random()
//        var n = 10000
//        n = generator.nextInt(n)
//        val fname = "Image-$n.jpg"
//        val file = File(myDir, fname)
//        Log.i("sssss", "" + file)
//        if (file.exists()) file.delete()
//        try {
//            val out = FileOutputStream(file)
//            dow?.compress(Bitmap.CompressFormat.JPEG, 2, out)
//            out.flush()
//            out.close()
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Log.e("nooo", "fail")
//        }
    }
}