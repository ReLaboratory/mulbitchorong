package com.repro.waterlight.information

import android.app.Activity
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.repro.waterlight.R
import com.repro.waterlight.home.Home
import com.repro.waterlight.main.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.io.InputStream
import java.util.regex.Pattern

class Login : AppCompatActivity() {
    private var auth: FirebaseAuth? = null

    private var email = ""
    private var password = ""
    private var passWordPattern: Pattern = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$")

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

        auth = FirebaseAuth.getInstance()

        loginNextBtn.setOnClickListener {
            email = loginEmail.text.toString()
            password = loginPw.text.toString()
            if(isValidEmail() && isValidPasswd()) {
                signInWithEmailAndPassword(email, password)
            }else{
                toast("입력이 잘못되었습니다.")
            }
        }

        forgot.setOnClickListener {
            startActivity<Signup>()
            finish()
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

    private fun signInWithEmailAndPassword(email: String, password: String){
        auth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val save = getSharedPreferences("savesign", Activity.MODE_PRIVATE).edit()
                    save.putBoolean("key", true)
                    save.apply()
                    toast("로그인 성공")
                    startActivity<Home>(
                        "check" to true
                    )
                    finish()
                } else {
                    toast("로그인 실패하셨습니다. 다시 확인해주시길 바랍니다.")
                }
            }
    }

    override fun onBackPressed() {
        startActivity<MainActivity>()
        finish()
    }
}
