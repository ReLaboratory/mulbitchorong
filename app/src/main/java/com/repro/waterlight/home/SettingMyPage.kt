package com.repro.waterlight.home

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.repro.waterlight.R
import com.repro.waterlight.file.UploadSuccess
import com.repro.waterlight.server.retro
import kotlinx.android.synthetic.main.activity_setting_my_page.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SettingMyPage : AppCompatActivity() {
    private val checkNum = 0
    private var filePath: Uri? = null
    private var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_my_page)

        //상단바 설정
        val ab: androidx.appcompat.app.ActionBar? = supportActionBar
        ab?.setIcon(R.mipmap.logo)
        ab?.setDisplayUseLogoEnabled(true)
        ab?.setDisplayShowHomeEnabled(true)

        id = intent.getStringExtra("id")

        val callProfile = retro.getClient.GetProfileImg(id)
        callProfile.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                Log.e("Pro2", "success")
                Log.e("Pro2", response?.body().toString())
                if(response?.body() != null) {
                    val bmp: Bitmap = BitmapFactory.decodeStream(response.body()!!.byteStream())
                    Glide.with(this@SettingMyPage).load(bmp).apply(RequestOptions.circleCropTransform()).into(myImage)
                }
            }
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                Log.e("Pro2", "fail")
                Log.e("Pro2", t.toString())
            }
        })

        myImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(intent, checkNum)
        }

        reserve.setOnClickListener {
            setting()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == checkNum) {
            filePath = data?.data
            if (filePath != null){
                Glide.with(this).load(filePath)
                    .apply(RequestOptions.circleCropTransform())
                    .into(myImage)
            }
        }
    }

    private fun setting() {
        val callGPR = retro.getClient.GetProfileRegistered(id)
        callGPR.enqueue(object : Callback<UploadSuccess>{
            override fun onFailure(call: Call<UploadSuccess>, t: Throwable) {
                Log.e("Pro3", "fail")
                Log.e("Pro3", t.toString())
            }
            override fun onResponse(call: Call<UploadSuccess>, response: Response<UploadSuccess>) {
                Log.e("Pro3", "success")
                Log.e("Pro3", response.body().toString())
                if(!response.isSuccessful){
                    val a: String = getRealPathFromURIPath(filePath)
                    val file = File(a)
                    val requestFile: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
                    val multipartBody: MultipartBody.Part = MultipartBody.Part.createFormData("profile", file.name, requestFile)
                    val requestBody: Map<String, String> = hashMapOf("uid" to id)

                    Log.e("Prou", filePath.toString())
                    Log.e("Prou", a)
                    Log.e("Prou", requestFile.toString())
                    Log.e("Prou", file.toString())
                    Log.e("Prou", multipartBody.toString())
                    Log.e("Prou", requestBody.toString())

                    val Cput = retro.getClient.PutRegisterProfile(multipartBody, requestBody)
                    Cput.enqueue(object: Callback<UploadSuccess>{
                        override fun onFailure(call: Call<UploadSuccess>, t: Throwable) {
                            Log.e("Prou", "fail")
                            Log.e("Prou", t.toString())
                        }
                        override fun onResponse(call: Call<UploadSuccess>, response: Response<UploadSuccess>) {
                            Log.e("Prou", "success")
                            Log.e("Prou", response.body()?.isSuccess.toString())
                            if(response.body()?.isSuccess!!){
                                startActivity<Home>(
                                    "check" to true,
                                    "id" to id
                                )
                                finish()
                            }
                        }
                    })
                }else{
                    val a: String = getRealPathFromURIPath(filePath)
                    val file = File(a)
                    val requestFile: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
                    val multipartBody: MultipartBody.Part = MultipartBody.Part.createFormData("profile", file.name, requestFile)
                    val requestBody: Map<String, String> = hashMapOf("uid" to id)

                    Log.e("Proo", filePath.toString())
                    Log.e("Proo", a)
                    Log.e("Proo", requestFile.toString())
                    Log.e("Proo", file.toString())
                    Log.e("Proo", multipartBody.toString())
                    Log.e("Proo", requestBody.toString())

                    val Cpost = retro.getClient.PostRegisterProfile(multipartBody, requestBody)
                    Cpost.enqueue(object: Callback<UploadSuccess>{
                        override fun onFailure(call: Call<UploadSuccess>, t: Throwable) {
                            Log.e("Proo", "fail")
                            Log.e("Proo", t.toString())
                        }
                        override fun onResponse(call: Call<UploadSuccess>, response: Response<UploadSuccess>) {
                            Log.e("Proo", "success")
                            Log.e("Proo", response.body()?.isSuccess.toString())
                            if(response.body()?.isSuccess!!){
                                startActivity<Home>(
                                    "check" to true,
                                    "id" to id
                                )
                                finish()
                            }
                        }
                    })
                }
            }
        })
    }

    @SuppressLint("Recycle")
    private fun getRealPathFromURIPath(contentURI: Uri?): String {
        val cursor: Cursor? = this.contentResolver.query(contentURI, null, null, null, null)
        return if (cursor == null) {
            contentURI?.path!!
        } else {
            cursor.moveToFirst()
            val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            cursor.getString(idx)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<Home>(
            "check" to true,
            "id" to id
        )
        finish()
    }
}
