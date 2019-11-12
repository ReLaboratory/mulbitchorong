package com.repro.waterlight.information

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.repro.waterlight.R
import com.repro.waterlight.file.SignupSuccess
import com.repro.waterlight.server.retro
import kotlinx.android.synthetic.main.activity_signup.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.InputStream
import java.util.regex.Pattern

class Signup : AppCompatActivity() {
    private var id = ""
    private var password = ""
    private var passWordPattern: Pattern = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val am: AssetManager = resources.assets
        val iss: InputStream = am.open("infor.png")
        val bm: Bitmap = BitmapFactory.decodeStream(iss)
        SignUpImg.setImageBitmap(bm)
        iss.close()
        SignUpEmail.background.setColorFilter(resources.getColor(R.color.water), PorterDuff.Mode.SRC_ATOP)
        SignUpPw.background.setColorFilter(resources.getColor(R.color.water), PorterDuff.Mode.SRC_ATOP)
        SignUpConfirmPw.background.setColorFilter(resources.getColor(R.color.water), PorterDuff.Mode.SRC_ATOP)
        name.background.setColorFilter(resources.getColor(R.color.water), PorterDuff.Mode.SRC_ATOP)

        signupNextBtn.setOnClickListener {
            id = SignUpEmail.text.toString()
            password = SignUpPw.text.toString()
            val conPw = SignUpConfirmPw.text.toString()

            if(isValidEmail() && isValidPasswd()) {
                if (password == conPw){
                    if (name.text.toString() != ""){
                        createUser()
                    }else{
                        toast("이름을 작성해주시기 바랍니다.")
                    }
                }else{
                    toast("비밀번호가 다릅니다.")
                }
            }else{
                toast("이메일 또는 비밀번호에서 오류가 생겼습니다.(비밀번호 4자리 이상)")
            }
        }
    }

    private fun isValidEmail(): Boolean {
        return id.isNotEmpty()
    }

    private fun isValidPasswd(): Boolean {
        return if (password.isEmpty()) {
            false
        } else passWordPattern.matcher(password).matches()
    }

    private fun createUser() {
        val requestBody: Map<String, String> = hashMapOf("uid" to id, "pw" to password, "uname" to name.text.toString())

        val call = retro.getClient.postSignup(requestBody)
        call.enqueue(object : Callback<SignupSuccess> {
            override fun onResponse(call: Call<SignupSuccess>?, response: Response<SignupSuccess>?) {
                Log.d("signup", "success")
                Log.d("signup", response?.body().toString())
                if(response?.isSuccessful!!){
                    Log.d("signup", response.code().toString())
                    Log.d("signup", response.body()?.uname)
                    Log.d("signup", response.body()?.isSuccess.toString())

                    if(response.body()?.isSuccess!!){
                        toast("환영합니다 ${response.body()?.uname}님")
                        finish()
                    }else{
                        toast("오류가 발생했습니다. 앱을 다시 실행시켜주십시오")
                    }
                }
            }
            override fun onFailure(call: Call<SignupSuccess>?, t: Throwable?) {
                Log.e("signup", "fail")
                toast("오류가 발생했습니다. 앱을 다시 실행시켜주십시오")
            }
        })
    }
}