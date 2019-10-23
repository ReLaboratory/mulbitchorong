package com.repro.waterlight.information

import android.annotation.SuppressLint
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.repro.waterlight.R
import com.repro.waterlight.file.UserDTO
import kotlinx.android.synthetic.main.activity_signup.*
import org.jetbrains.anko.toast
import java.io.InputStream
import java.util.regex.Pattern

class Signup : AppCompatActivity() {
    private var auth: FirebaseAuth? = null

    private var email = ""
    private var password = ""
    private var passWordPattern: Pattern = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()

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
            email = SignUpEmail.text.toString()
            password = SignUpPw.text.toString()
            val conPw = SignUpConfirmPw.text.toString()

            if(isValidEmail() && isValidPasswd()) {
                if (password == conPw){
                    if (name.text.toString() != ""){
                        createUser(email, password)
                    }else{
                        toast("이름을 작성해주시기 바랍니다.")
                    }
                }else{
                    toast("비밀번호가 다릅니다.")
                }
            }else{
                toast("이메일 또는 비밀번호에서 오류가 생겼습니다.(비밀번호 8자리 이상)")
            }
        }
    }

    private fun isValidEmail(): Boolean {
        return if (email.isEmpty()) {
            false
        } else Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPasswd(): Boolean {
        return if (password.isEmpty()) {
            false
        } else passWordPattern.matcher(password).matches()
    }

    @SuppressLint("CommitPrefEdits")
    private fun createUser(email: String, password: String) {
        auth?.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(this) { p0 ->
            if (p0.isSuccessful) {
                val fire: FirebaseFirestore = FirebaseFirestore.getInstance()
                val user = UserDTO(email, name.text.toString(), null, password)

                fire.collection("users").document(email).set(user)

                toast("회원가입 성공")
                finish()
            } else {
                toast("오류가 발생하였습니다. 다시 시도 하시길 바랍니다.")
            }
        }
    }
}