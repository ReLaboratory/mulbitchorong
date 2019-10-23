package com.repro.waterlight.home

import android.os.Bundle
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.repro.waterlight.R
import com.repro.waterlight.file.ContentDTO
import kotlinx.android.synthetic.main.fragment_grid.view.*
import java.util.*

class GridFragment: Fragment() {
    var imageSnapshot: ListenerRegistration? = null
    private var mainView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainView = inflater.inflate(R.layout.fragment_grid, container, false)

        return mainView
    }

    override fun onResume() {
        super.onResume()
        mainView?.gridfragment_recyclerview?.adapter = GridFragmentRecyclerViewAdapter()
        mainView?.gridfragment_recyclerview?.layoutManager = GridLayoutManager(activity, 3)
    }

    inner class GridFragmentRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

        private var ContentDTOS: ArrayList<ContentDTO> = ArrayList()

        init {
            imageSnapshot = FirebaseFirestore
                .getInstance().collection("images")
                .addSnapshotListener { querySnapshot, _ ->
                    ContentDTOS.clear()
                    if (querySnapshot == null){
                        return@addSnapshotListener
                    }
                    for (snapshot in querySnapshot.documents) {
                        ContentDTOS.add(snapshot.toObject(ContentDTO::class.java)!!)
                    }
                    notifyDataSetChanged()
                }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            //현재 사이즈 뷰 화면 크기의 가로 크기의 1/3값을 가지고 오기
            val width = resources.displayMetrics.widthPixels / 3

            val imageView = ImageView(parent.context)
            imageView.layoutParams = LinearLayoutCompat.LayoutParams(width, width)
            return CustomViewHolder(imageView)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val imageView = (holder as CustomViewHolder).imageView

            Glide.with(holder.itemView.context)
                .load(ContentDTOS[position].imageuri)
                .apply(RequestOptions().centerCrop())
                .into(imageView)
        }

        override fun getItemCount(): Int {
            return ContentDTOS.size
        }

        inner class CustomViewHolder(var imageView: ImageView) : RecyclerView.ViewHolder(imageView)
    }
}