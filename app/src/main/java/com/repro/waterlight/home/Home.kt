package com.repro.waterlight.home

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.repro.waterlight.R
import com.repro.waterlight.file.UpLoad
import com.repro.waterlight.file.UserDTO
import com.repro.waterlight.main.MainActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.header.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class Home : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var storage: FirebaseStorage? = null
    private var mStorageRef: StorageReference? = null
    private var store: FirebaseFirestore? = null
    private var auth: FirebaseAuth? = null
    private var check: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //상단바 설정
        val ab: androidx.appcompat.app.ActionBar? = supportActionBar
        ab?.setIcon(R.mipmap.logo)
        ab?.setDisplayUseLogoEnabled(true)
        ab?.setDisplayShowHomeEnabled(true)

        //네비게이션 설정
        homeSideMenu.setNavigationItemSelectedListener(this)
        check = intent.getBooleanExtra("check", false)

        storage = FirebaseStorage.getInstance()
        mStorageRef = storage!!.getReferenceFromUrl("gs://waterlight-10fe7.appspot.com/").child("수정/y")
        store = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        //사진 가져오기
        val gridFragment = GridFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_content, gridFragment)
            .commit()

        val docRef = store!!.collection("users").document(auth!!.currentUser?.email.toString())
        docRef.get().addOnSuccessListener { documentSnapshot ->
            val city = documentSnapshot.toObject(UserDTO::class.java)
            homeSideMenu.setNavigationItemSelectedListener(this)

            //이름
            if(city?.name != null) {
                name.text = city.name
            }else{
                name.text = "YourName"
            }

            //프로필
            if(city?.profiluri != null){
                Glide.with(this).load(city.profiluri)
                    .apply(RequestOptions.circleCropTransform())
                    .into(profile)
            }else{
                Glide.with(this).load(R.mipmap.user_foreground).into(profile)
            }
        }
    }

    //상단바
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.sideBtn) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawers()
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    //바디
    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        if (this.check!!) {
            when (p0.itemId) {
                R.id.profil->{
                    startActivity<SettingMyPage>()
                }
                R.id.fileUpLoad -> {
                    startActivity<UpLoad>()
                }
                R.id.logOut -> {
                    auth?.signOut()
                    startActivity<MainActivity>()
                    finish()
                }
            }
        }else{
            toast("""해당 메뉴 사용하실 권한이 없습니다. 
                |로그인을 하시길 바랍니다.""".trimMargin())
        }

        drawerLayout.closeDrawers()
        return true
    }

    //메뉴
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_bar, menu)
        return true
    }

    override fun onBackPressed() {
        if (auth?.currentUser?.email != null) {
            val save = getSharedPreferences("saveLogin", Activity.MODE_PRIVATE).edit()
            save.putString("keyE", auth?.currentUser?.email)
            save.apply()
        }
        super.onBackPressed()
    }
}