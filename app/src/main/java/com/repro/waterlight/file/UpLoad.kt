package com.repro.waterlight.file

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.repro.waterlight.R
import kotlinx.android.synthetic.main.activity_up_load.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.jetbrains.anko.toast
import java.io.File

class UpLoad : AppCompatActivity() {
    private val checkNum = 0
    private var filePath: Uri? = null
//    private var storage: FirebaseStorage? = null
//    private var data: FirebaseFirestore? = null
//    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_up_load)

        //상단바 설정
        val ab: androidx.appcompat.app.ActionBar? = supportActionBar
        ab?.setIcon(R.mipmap.logo)
        ab?.setDisplayUseLogoEnabled(true)
        ab?.setDisplayShowHomeEnabled(true)

//        //파이어베이스 설정
//        storage = FirebaseStorage.getInstance()
//        data = FirebaseFirestore.getInstance()
//        auth = FirebaseAuth.getInstance()

        //이미지 선택
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, checkNum)

        fileUpBtn.setOnClickListener {
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
//            val storageRef = storage?.getReferenceFromUrl("gs://waterlight-10fe7.appspot.com")?.child("images")
//                ?.child(fileName)

            val fileUri: String =  getRealPathFromURIPath(filePath!!)
            uploadToServer(fileUri)

//            storageRef!!.putFile(filePath!!)
//                .addOnSuccessListener {
//                    val uploadTask = storageRef.putFile(filePath!!)
//                    uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> {
//                        return@Continuation storageRef.downloadUrl
//                    }).addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            uri = task.result
//
//                            val content = ContentDTO()
//
//                            content.imageuri = uri.toString()
//                            content.userId = auth?.currentUser?.email
//                            content.time = SimpleDateFormat("yyyyMMHH_mmss").format(Date())
//                            content.name = fileName
//
//                            data?.collection("images")?.document()?.set(content)
//                            setResult(Activity.RESULT_OK)
//                            finish()
//                            toast("앱을 다시 실행시키면 적용됩니다.")
//                        }
//                    }
//                }
//                .addOnFailureListener { toast("업로드 실패") }
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
        val requestFile: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
//        val part = MultipartBody.Part.createFormData(imageName.text.toString(), file.name, requestFile)
//            val call = retro.getClient.uploadImage(part)
//            call.enqueue(object : Callback<SignSuccess> {
//                override fun onResponse(call: Call<SignSuccess>?, response: Response<SignSuccess>?) {
//                }
//                override fun onFailure(call: Call<SignSuccess>?, t: Throwable?) {
//                }
//            })
    }
}
