package com.zotikos.m4u.ui.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zotikos.m4u.R
import com.zotikos.m4u.databinding.PostListFragmentBinding

class PostListFragment : Fragment() {

    companion object {
        const val FRAGMENT_TAG = "PostListFragment"

        fun newInstance() = PostListFragment()
    }

    private lateinit var viewModel: PostListViewModel

    private lateinit var binding: PostListFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(PostListViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.post_list_fragment, container, false)
        binding.postList.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.viewModel = viewModel
        return binding.root
    }

}
