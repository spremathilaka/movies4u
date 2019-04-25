package com.zotikos.m4u.ui.posts

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zotikos.m4u.databinding.FragmentPostListBinding
import com.zotikos.m4u.di.module.ViewModelFactory
import com.zotikos.m4u.ui.base.BaseFragment
import com.zotikos.m4u.ui.vo.PostUIDto
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class PostListFragment : BaseFragment() {

    companion object {
        const val FRAGMENT_TAG = "PostListFragment"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: PostListViewModel

    private lateinit var binding: FragmentPostListBinding

    private lateinit var postListAdapter: PostListAdapter

    private var listener: PostListFragment.OnFragmentInteractionListener? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(PostListViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        viewModel.loadPosts()

        binding = DataBindingUtil.inflate(inflater, com.zotikos.m4u.R.layout.fragment_post_list, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycleView()
        observeViewModel()
    }

    private fun setupRecycleView() {
        binding.postList.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        postListAdapter = PostListAdapter { postItem: PostUIDto -> postItemClicked(postItem) }
        binding.postList.adapter = postListAdapter
        binding.postList.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

    }

    private fun observeViewModel() {

        viewModel.getPosts().observe(viewLifecycleOwner, Observer { event ->
            val action: PostsListAction? = event?.getContentIfNotHandled()

            when (action) {
                is PostsListAction.PostsLoadingSuccess -> {
                    postListAdapter.updatePostList(action.posts)
                }
            }
        })

        subscribeCommonAction(viewModel.commonViewActionEvent/*, viewModel.loadingIndicator*/)
    }

    override fun onAttach(context: Context) {
        //with out this error : late init property viewModelFactory has not been initialized
        AndroidSupportInjection.inject(this)
        if (context is PostListFragment.OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
        super.onAttach(context)
    }


    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    override fun layoutRes(): Int {
        return com.zotikos.m4u.R.layout.fragment_post_list
    }


    private fun postItemClicked(postItem: PostUIDto) {
        Toast.makeText(activity, "Clicked: ${postItem.title}", Toast.LENGTH_LONG).show()
        listener?.showPostDetail(postItem)
    }

    interface OnFragmentInteractionListener {
        fun showPostDetail(post: PostUIDto)
    }

}
