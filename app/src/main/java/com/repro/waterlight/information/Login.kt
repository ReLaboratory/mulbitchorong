package com.repro.waterlight.information

import android.app.Activity
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.repro.waterlight.R
import com.repro.waterlight.file.SignSuccess
import com.repro.waterlight.home.Home
import com.repro.waterlight.main.MainActivity
import com.repro.waterlight.server.retro
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.InputStream

class Login : AppCompatActivity() {
    private var id = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val am: AssetManager = resources.assets
        val iss: InputStream = am.open("infor.png")
        val bm: Bitmap = BitmapFactory.decodeStream(iss)
        LoginImg.setImageBitmap(bm)
        iss.close()
        loginEmail.background.setColorFilter(resources.getColor(R.color.water), PorterDuff.Mode.SRC_ATOP)
        loginPw.background.setColorFilter(resources.getColor(R.color.water), PorterDuff.Mode.SRC_ATOP)

        loginNextBtn.setOnClickListener {
            id = loginEmail.text.toString()
            password = loginPw.text.toString()
            if(isValidEmail() && isValidPasswd()) {
                signInWithEmailAndPassword()
            }else{
                toast("입력이 잘못되었습니다.")
            }
        }
    }

    private fun isValidEmail(): Boolean {
        return id.isNotEmpty()
    }

    private fun isValidPasswd(): Boolean {
        return password.isNotEmpty()
    }

    private fun signInWithEmailAndPassword(){
        val requestBody: Map<String, String> = hashMapOf("uid" to id, "pw" to password)

        val call = retro.getClient.postLogin(requestBody)
        call.enqueue(object : Callback<SignSuccess> {
            override fun onResponse(call: Call<SignSuccess>?, response: Response<SignSuccess>?) {
                Log.d("signin", "success")
                Log.d("signin", response?.body().toString())
                if(response?.isSuccessful!!){
                    Log.d("signin", response.code().toString())
                    Log.d("signin", response.body()?.uname)
                    Log.d("signin", response.body()?.isSuccess.toString())

                    if(response.body()?.isSuccess!!){
                        val save = getSharedPreferences("savesign", Activity.MODE_PRIVATE).edit()
                        save.putBoolean("key", response.body()!!.isSuccess!!)
                        save.putString("id", id)
                        save.apply()
                        toast("로그인 성공")
                        startActivity<Home>(
                            "check" to true,
                            "id" to id
                        )
                        finish()
                    }else{
                        toast("오류가 발생했습니다. 앱을 다시 실행시켜주십시오")
                    }
                }
            }
            override fun onFailure(call: Call<SignSuccess>?, t: Throwable?) {
                Log.e("signin", "fail")
                toast("로그인 실패하셨습니다. 다시 확인해주시길 바랍니다.")
            }
        })
    }

    override fun onBackPressed() {
        startActivity<MainActivity>()
        finish()
    }
}