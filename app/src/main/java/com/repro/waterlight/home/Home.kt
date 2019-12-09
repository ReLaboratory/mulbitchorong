package com.repro.waterlight.home

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.navigation.NavigationView
import com.repro.waterlight.R
import com.repro.waterlight.file.GetName
import com.repro.waterlight.file.GetimgNames
import com.repro.waterlight.file.UpLoad
import com.repro.waterlight.main.MainActivity
import com.repro.waterlight.server.retro
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.header.*
import okhttp3.ResponseBody
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Home : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var check: Boolean? = null
    private var id: String = ""
    private var imgNames: ArrayList<GetimgNames> = ArrayList()
    private var imgs: ArrayList<Bitmap> = ArrayList()
    private var imm = 0

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
        id = intent.getStringExtra("id")
        val callName = retro.getClient.GetUserName(id)
        callName.enqueue(object : Callback<GetName> {
            override fun onResponse(call: Call<GetName>?, response: Response<GetName>?) {
                Log.e("Home1", "success")
                Log.e("Home1", response?.body().toString())
                if(response?.isSuccessful!!){
                    Log.e("Home1", response.code().toString())
                    Log.e("Home1", response.body().toString())
                    if(response.body()?.uname != null && response.body() != null && response.body()!!.uname != "") {
                        name.text = response.body()!!.uname
                    }
                }
            }
            override fun onFailure(call: Call<GetName>?, t: Throwable?) {
                Log.e("Home1", "fail")
                Log.e("Home1", t.toString())
                name.text = "YOUR NAME"
            }
        })

        //모든 이미지 이름 가져옴
        val callImgName = retro.getClient.GetImgNames()
        callImgName.enqueue(object : Callback<ArrayList<GetimgNames>> {
            override fun onResponse(call: Call<ArrayList<GetimgNames>>?, response: Response<ArrayList<GetimgNames>>?) {
                Log.e("Home2", "success")
                Log.e("Home2", response?.body().toString())
                imgNames = response?.body()!!

                for (i in imgNames) {
                    imm++
                    val callImgs = retro.getClient.GetImage(i.imgName)
                    callImgs.enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                            Log.e("Home3", "success")
                            Log.e("Home3", response?.body().toString())
                            Log.e("Home3", response?.body()?.byteStream().toString())
                            imgs.add(BitmapFactory.decodeStream(response?.body()?.byteStream()))
                            Log.e("Home3", imgs.size.toString())

                            if(imm == imgNames.size){
                                //사진 가져오기
                                val gridFragment = GridFragment(this@Home, id)
                                supportFragmentManager
                                    .beginTransaction()
                                    .replace(R.id.main_content, gridFragment)
                                    .commit()
                                val bun = Bundle()
                                bun.putSerializable("obj", imgs)
                                bun.putSerializable("name", imgNames)
                                gridFragment.arguments = bun

                                Log.e("ok", "ok")
                            }
                        }
                        override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                            Log.e("Home3", "fail")
                            Log.e("Home3", t.toString())
                        }
                    })
                }
            }
            override fun onFailure(call: Call<ArrayList<GetimgNames>>, t: Throwable) {
                Log.e("Home2", "fail")
                Log.e("Home2", t.toString())
            }
        })

        //프로필 가져옴
        val callProfile = retro.getClient.GetProfileImg(id)
        callProfile.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                Log.e("Pro1", "success")
                Log.e("Pro1", response?.body().toString())
                if(response?.body() != null) {
                    val bmp: Bitmap = BitmapFactory.decodeStream(response.body()!!.byteStream())
                    Glide.with(this@Home).load(bmp).apply(RequestOptions.circleCropTransform()).into(profile)
                }
            }
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                Log.e("Pro1", "fail")
                Log.e("Pro1", t.toString())
            }
        })
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
    @SuppressLint("CommitPrefEdits", "LongLogTag")
    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        if (this.check!!) {
            when (p0.itemId) {
                R.id.profil->{
                    startActivity<SettingMyPage>(
                        "id" to id
                    )
                    finish()
                }
                R.id.fileUpLoad -> {
                    startActivity<UpLoad>(
                        "aaa" to id
                    )
                    finish()
                }
                R.id.logOut -> {
                    val save = getSharedPreferences("savesign", Activity.MODE_PRIVATE).edit()
                    save.clear()
                    save.apply()
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
}