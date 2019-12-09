package com.repro.waterlight.home

import android.graphics.Bitmap
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
import kotlinx.android.synthetic.main.fragment_grid.view.*
import org.jetbrains.anko.startActivity
import java.io.ByteArrayOutputStream
import java.util.*


class GridFragment(thi: Home, id: String): Fragment() {
    private var mainView: View? = null
    private var imgs: ArrayList<Bitmap> = ArrayList()
    private var imgsName: ArrayList<GetimgNames> = ArrayList()
    private var a = thi
    private var id2 = id

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainView = inflater.inflate(R.layout.fragment_grid, container, false)

        if(arguments != null){
            imgs = arguments!!.getSerializable("obj") as ArrayList<Bitmap>
            imgsName = arguments!!.getSerializable("name") as ArrayList<GetimgNames>
        }

        Log.e("ok", "onCreateView")
        return mainView
    }

    override fun onResume() {
        super.onResume()
        mainView?.gridfragment_recyclerview?.adapter = GridFragmentRecyclerViewAdapter()
        mainView?.gridfragment_recyclerview?.layoutManager = GridLayoutManager(activity, 3)
        Log.e("ok", "onResume")
    }


    inner class GridFragmentRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            //현재 사이즈 뷰 화면 크기의 가로 크기의 1/3값을 가지고 오기
            val width = resources.displayMetrics.widthPixels / 3

            val imageView = ImageView(parent.context)
            imageView.layoutParams = LinearLayoutCompat.LayoutParams(width, width)
            Log.e("ok", "onCreateViewHolder")
            return CustomViewHolder(imageView)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val imageView = (holder as CustomViewHolder).imageView

            Glide.with(holder.itemView.context)
                .load(imgs[position])
                .apply(RequestOptions().centerCrop())
                .into(imageView)

            imageView.setOnClickListener {
                Log.e("ok", "set1")

                val stream = ByteArrayOutputStream()
                imgs[position].compress(Bitmap.CompressFormat.PNG, 2, stream)
                val bytes = stream.toByteArray()
                context?.startActivity<Down>(
                    "file" to bytes,
                    "name" to imgsName[position].imgName.toString(),
                    "id" to id2
                )
                a.finish()
                Log.e("ok", "set2")
            }

            Log.e("ok", "onBindViewHolder")
        }

        override fun getItemCount(): Int {
            Log.e("ok", "count")
            return imgs.size
        }

        inner class CustomViewHolder(var imageView: ImageView) : RecyclerView.ViewHolder(imageView)
    }
}