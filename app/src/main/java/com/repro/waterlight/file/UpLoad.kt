package com.repro.waterlight.file

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.repro.waterlight.R
import com.repro.waterlight.home.Home
import com.repro.waterlight.server.retro
import kotlinx.android.synthetic.main.activity_up_load.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UpLoad : AppCompatActivity() {
    private val checkNum = 0
    private var filePath: Uri? = null
    var id: String = ""
    var name: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_up_load)

        //상단바 설정
        val ab: androidx.appcompat.app.ActionBar? = supportActionBar
        ab?.setIcon(R.mipmap.logo)
        ab?.setDisplayUseLogoEnabled(true)
        ab?.setDisplayShowHomeEnabled(true)

        id = intent.getStringExtra("aaa")

        //이미지 선택
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, checkNum)

        fileUpBtn.setOnClickListener {
            name = imageName.text.toString()
            uploadFile()
        }
        reChoiceBtn.setOnClickListener {
            val reInten = Intent(Intent.ACTION_PICK)
            reInten.type = MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(reInten, checkNum)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == checkNum) {
            filePath = data?.data
            if (filePath != null){
                selectImage.setImageURI(filePath)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun uploadFile() {
        //업로드할 파일이 있으면 수행
        if (filePath != null) {
            val fileUri: String =  getRealPathFromURIPath(filePath!!)
            uploadToServer(fileUri)
        }else{
            toast("파일을 선택해 주시기 바랍니다.")
        }
    }

    @SuppressLint("Recycle")
    private fun getRealPathFromURIPath(contentURI: Uri): String {
        val cursor: Cursor? = this.contentResolver.query(contentURI, null, null, null, null)
        return if (cursor == null) {
            contentURI.path
        } else {
            cursor.moveToFirst()
            val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            cursor.getString(idx)
        }
    }

    private fun uploadToServer(fileURI: String) {
        val file = File(fileURI)
        val requestFile: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val multipartBody: MultipartBody.Part = MultipartBody.Part.createFormData("files", file.name, requestFile)
        val requestBody: Map<String, String> = hashMapOf("uid" to id, "imgName" to name)

        Log.e("upload", "$requestBody | $file | $fileURI | $requestFile | $multipartBody ${requestFile.contentType().toString()}")
        Log.e("upload", "${requestBody["imgName"]} | ${requestBody["uid"]}")

        val call = retro.getClient.Upload(multipartBody, requestBody)
        call.enqueue(object : Callback<UploadSuccess> {
            override fun onFailure(call: Call<UploadSuccess>, t: Throwable) {
                Log.e("upload", t.toString())
                Log.e("upload", t.cause.toString())
            }
            override fun onResponse(call: Call<UploadSuccess>, response: Response<UploadSuccess>) {
                Log.e("upload", response.isSuccessful.toString())
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

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<Home>(
            "check" to true,
            "id" to id
        )
        finish()
    }
}