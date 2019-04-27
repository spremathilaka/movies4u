package com.zotikos.m4u.ui.post.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.transition.TransitionInflater
import com.zotikos.m4u.R
import com.zotikos.m4u.databinding.FragmentPostDetailBinding
import com.zotikos.m4u.di.module.ViewModelFactory
import com.zotikos.m4u.ui.base.BaseFragment
import com.zotikos.m4u.ui.vo.PostUIDto
import com.zotikos.m4u.util.extension.ImageLoadingCallback
import com.zotikos.m4u.util.extension.load
import dagger.android.support.AndroidSupportInjection
import timber.log.Timber
import javax.inject.Inject


class PostDetailFragment : BaseFragment(), ImageLoadingCallback {


    companion object {

        const val FRAGMENT_TAG = "PostDetailFragment"

        @JvmStatic
        fun newInstance() =
            PostDetailFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: PostDetailsViewModel

    private lateinit var binding: FragmentPostDetailBinding

    private var selectedPostItem: PostUIDto? = null

    private var listener: OnFragmentInteractionListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        selectedPostItem = arguments?.let { PostDetailFragmentArgs.fromBundle(it).postDetails }

        viewModel =
            ViewModelProviders.of(this@PostDetailFragment, viewModelFactory).get(PostDetailsViewModel::class.java)
        viewModel.postItem = selectedPostItem

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("ImageURL " + selectedPostItem?.imageUrl)
        binding.postImageView.load(selectedPostItem?.imageUrl, this)
        //  postponeEnterTransition()
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



    override fun layoutRes(): Int = R.layout.fragment_post_detail


    override fun onLoadingSuccess() {
        Timber.d("onLoadingSuccess")
        startPostponedEnterTransition()
    }

    override fun onError(e: Exception?) {
        startPostponedEnterTransition()
        Timber.d("onError")
    }
}
