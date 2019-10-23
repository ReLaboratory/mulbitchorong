package com.repro.waterlight.file

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.repro.waterlight.R
import kotlinx.android.synthetic.main.activity_up_load.*
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*

class UpLoad : AppCompatActivity() {
    private val checkNum = 0
    private var filePath: Uri? = null
    private var storage: FirebaseStorage? = null
    private var data: FirebaseFirestore? = null
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_up_load)

        //상단바 설정
        val ab: androidx.appcompat.app.ActionBar? = supportActionBar
        ab?.setIcon(R.mipmap.logo)
        ab?.setDisplayUseLogoEnabled(true)
        ab?.setDisplayShowHomeEnabled(true)

        //파이어베이스 설정
        storage = FirebaseStorage.getInstance()
        data = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

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
        //request코드가 0이고 OK를 선택했고 data에 뭔가가 들어 있다면
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
            val fileName = SimpleDateFormat("yyyyMMHH_mmss").format(Date())
            val storageRef = storage?.getReferenceFromUrl("gs://waterlight-10fe7.appspot.com")?.child("images")
                ?.child(fileName)
            var uri: Uri?

            storageRef!!.putFile(filePath!!)
                .addOnSuccessListener {
                    val uploadTask = storageRef.putFile(filePath!!)
                    uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> {
                        return@Continuation storageRef.downloadUrl
                    }).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            uri = task.result

                            val content = ContentDTO()

                            content.imageuri = uri.toString()
                            content.uid = auth?.currentUser?.uid
                            content.userId = auth?.currentUser?.email
                            content.time = fileName

                            data?.collection("images")?.document()?.set(content)
                            setResult(Activity.RESULT_OK)
                            finish()
                            toast("앱을 다시 실행시키면 적용됩니다.")
                        }
                    }
                }
                .addOnFailureListener { toast("업로드 실패") }
        }else{
            toast("파일을 선택해 주시기 바랍니다.")
        }
    }
}
