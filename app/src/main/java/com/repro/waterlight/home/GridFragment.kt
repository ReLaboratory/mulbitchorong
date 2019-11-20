package com.repro.waterlight.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.repro.waterlight.R
import com.repro.waterlight.file.Down
import com.repro.waterlight.file.GetimgNames
import com.repro.waterlight.server.retro
import kotlinx.android.synthetic.main.fragment_grid.view.*
import okhttp3.ResponseBody
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class GridFragment: Fragment() {
//    var imageSnapshot: ListenerRegistration? = null
    private var mainView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainView = inflater.inflate(R.layout.fragment_grid, container, false)

        return mainView
    }

    override fun onResume() {
        super.onResume()
        mainView?.gridfragment_recyclerview?.adapter = GridFragmentRecyclerViewAdapter()
        mainView?.gridfragment_recyclerview?.layoutManager = GridLayoutManager(activity, 3)
    }

    //yes
    inner class GridFragmentRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
//        private var ContentDTOS: ArrayList<ContentDTO> = ArrayList()
        private var imgNames: ArrayList<GetimgNames> = ArrayList()
        private var imgs: ArrayList<Bitmap> = ArrayList()

        init {
            //모든 이미지 이름 가져옴
            val callImgName = retro.getClient.GetImgNames()
            callImgName.enqueue(object : Callback<ArrayList<GetimgNames>> {
                override fun onResponse(call: Call<ArrayList<GetimgNames>>?, response: Response<ArrayList<GetimgNames>>?) {
                    Log.e("Home2", "success")
                    Log.e("Home2", response?.body().toString())
                    imgNames = response?.body()!!

                    for (i in imgNames) {
                        val callImgs = retro.getClient.GetImage(i.imgName)
                        callImgs.enqueue(object : Callback<ResponseBody> {
                            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                                Log.e("Home3", "success")
                                Log.e("Home3", response?.body().toString())
                                Log.e("Home3", response?.body()?.byteStream().toString())
                                imgs.add(BitmapFactory.decodeStream(response?.body()?.byteStream()))
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

//            imageSnapshot = FirebaseFirestore
//                .getInstance().collection("images")
//                .addSnapshotListener { querySnapshot, _ ->
//                    ContentDTOS.clear()
//                    if (querySnapshot == null){
//                        return@addSnapshotListener
//                    }
//                    for (snapshot in querySnapshot.documents) {
//                        ContentDTOS.add(snapshot.toObject(ContentDTO::class.java)!!)
//                    }
//                    notifyDataSetChanged()
//                }
        }

        //no
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            //현재 사이즈 뷰 화면 크기의 가로 크기의 1/3값을 가지고 오기
            val width = resources.displayMetrics.widthPixels / 3

            val imageView = ImageView(parent.context)
            imageView.layoutParams = LinearLayoutCompat.LayoutParams(width, width)
            return CustomViewHolder(imageView)
        }

        //yes
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val imageView = (holder as CustomViewHolder).imageView

            Glide.with(holder.itemView.context)
                .load(imgs[position])
                .apply(RequestOptions().centerCrop())
                .into(imageView)

//            Glide.with(holder.itemView.context)
//                .load(ContentDTOS[position].imageuri)
//                .apply(RequestOptions().centerCrop())
//                .into(imageView)

            imageView.setOnClickListener {
                holder.itemView.context.startActivity<Down>()
            }
        }

        //yes
        override fun getItemCount(): Int {
//            return ContentDTOS.size
            Log.e("git", imgs.size.toString())
            return imgs.size
        }

        inner class CustomViewHolder(var imageView: ImageView) : RecyclerView.ViewHolder(imageView)
    }
}