package com.zotikos.m4u.ui.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.zotikos.m4u.R
import com.zotikos.m4u.databinding.FragmentPostDetailBinding
import com.zotikos.m4u.di.module.ViewModelFactory
import com.zotikos.m4u.ui.base.BaseFragment
import com.zotikos.m4u.ui.vo.PostUIDto
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


private const val ARG_POST_ITEM = "post_item"


class PostDetailFragment : BaseFragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: PostDetailsViewModel

    private lateinit var binding: FragmentPostDetailBinding


    private var selectedPostItem: PostUIDto? = null
    private var listener: OnFragmentInteractionListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            selectedPostItem = it.getParcelable(ARG_POST_ITEM)
        }
        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(PostDetailsViewModel::class.java).also {
                it.postItem = selectedPostItem
            }
        } ?: throw Exception("Invalid Activity")
        observeViewModel()
    }


    private fun observeViewModel() {

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_detail, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }


    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnFragmentInteractionListener {
        //nothing
    }

    companion object {

        const val FRAGMENT_TAG = "PostDetailFragment"

        @JvmStatic
        fun newInstance(post: PostUIDto) =
            PostDetailFragment().apply {
                arguments = bundleOf(ARG_POST_ITEM to post)
            }
    }

    override fun layoutRes(): Int = R.layout.fragment_post_detail
}
