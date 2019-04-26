package com.zotikos.m4u.ui.post.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.zotikos.m4u.R
import com.zotikos.m4u.databinding.ItemPostBinding
import com.zotikos.m4u.ui.vo.PostUIDto

class PostListAdapter(val clickListener: (PostUIDto) -> Unit) : RecyclerView.Adapter<PostListAdapter.ViewHolder>() {

    private lateinit var postList: List<PostUIDto>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemPostBinding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_post, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(postList[position], clickListener)
    }

    override fun getItemCount(): Int {
        return if (::postList.isInitialized) postList.size else 0
    }

    fun updatePostList(postList: List<PostUIDto>) {
        this.postList = postList
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: PostUIDto, clickListener: (PostUIDto) -> Unit) {
            binding.postItem = post
            binding.root.setOnClickListener { clickListener(post) }
        }
    }
}