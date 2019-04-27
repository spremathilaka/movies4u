package com.zotikos.m4u.ui.post.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.zotikos.m4u.R
import com.zotikos.m4u.databinding.ItemPostBinding
import com.zotikos.m4u.ui.vo.PostUIDto

class PostListAdapter(
    private val context: Context,
    private val clickListener: (PostUIDto, ImageView, TextView, Int) -> Unit
) :
    RecyclerView.Adapter<PostListAdapter.ViewHolder>() {

    private lateinit var postList: List<PostUIDto>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemPostBinding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_post, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(postList[position], clickListener, position)
    }

    override fun getItemCount(): Int {
        return if (::postList.isInitialized) postList.size else 0
    }

    fun updatePostList(postList: List<PostUIDto>) {
        this.postList = postList
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: PostUIDto, clickListener: (PostUIDto, ImageView, TextView, Int) -> Unit, position: Int) {
            binding.postItem = post
            binding.postImageView.transitionName =
                "%s_%d".format(context.getString(R.string.hero_image_transition), position)
            binding.postTitle.transitionName = "%s_%d".format(context.getString(R.string.title_transition), position)
            binding.root.setOnClickListener { clickListener(post, binding.postImageView, binding.postTitle, position) }
        }
    }
}