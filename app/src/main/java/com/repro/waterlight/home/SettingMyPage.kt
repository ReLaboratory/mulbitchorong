package com.repro.waterlight.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.repro.waterlight.R
import kotlinx.android.synthetic.main.activity_setting_my_page.*

class SettingMyPage : AppCompatActivity() {
    private val checkNum = 0
    private var filePath: Uri? = null
//    private var store: FirebaseFirestore? = null
//    private var auth: FirebaseAuth? = null
//    private var storage: FirebaseStorage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_my_page)

        //상단바 설정
        val ab: androidx.appcompat.app.ActionBar? = supportActionBar
        ab?.setIcon(R.mipmap.logo)
        ab?.setDisplayUseLogoEnabled(true)
        ab?.setDisplayShowHomeEnabled(true)

//        store = FirebaseFirestore.getInstance()
//        auth = FirebaseAuth.getInstance()
//        storage = FirebaseStorage.getInstance()
//        val docRef = store!!.collection("users").document(auth!!.currentUser?.email.toString())
//        docRef.get().addOnSuccessListener { documentSnapshot ->
//            val city = documentSnapshot.toObject(UserDTO::class.java)
//            myName.hint = city?.name
//        }

        myImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(intent, checkNum)
        }

        reserve.setOnClickListener {
            setting()
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //request코드가 0이고 OK를 선택했고 data에 뭔가가 들어 있다면
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
//        val docRef = store!!.collection("users").document(auth!!.currentUser?.email.toString())
//        docRef.get().addOnSuccessListener { documentSnapshot ->
//            val city = documentSnapshot.toObject(UserDTO::class.java)
//            if(myName.text.toString() == "" || filePath == null){
//                toast("다시 설정해주시길 바랍니다")
//            }else {
//                if (filePath != null) {
//                    val storageRef = storage?.getReferenceFromUrl("gs://waterlight-10fe7.appspot.com")?.child("users")
//                        ?.child(auth!!.currentUser?.email.toString())
//                    var uri: Uri?
//
//                    storageRef!!.putFile(filePath!!)
//                        .addOnSuccessListener {
//                            val uploadTask = storageRef.putFile(filePath!!)
//                            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> {
//                                return@Continuation storageRef.downloadUrl
//                            }).addOnCompleteListener { task ->
//                                if (task.isSuccessful) {
//                                    uri = task.result
//                                    city?.name = myName.text.toString()
//                                    city?.profiluri = uri.toString()
//
//                                    val fire: FirebaseFirestore = FirebaseFirestore.getInstance()
//                                    fire.collection("users").document(auth!!.currentUser?.email.toString()).set(city!!)
//                                }
//                            }
//                        }
//                        .addOnFailureListener { toast("수정 실패") }
//                        .addOnProgressListener { toast("잠시만 기다려 주십시오") }
//                }
//            }
//        }
//        finish()
    }
}
