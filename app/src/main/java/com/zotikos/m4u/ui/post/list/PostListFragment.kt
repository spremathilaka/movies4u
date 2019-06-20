package com.zotikos.m4u.ui.post.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.transition.TransitionInflater
import com.zotikos.m4u.R
import com.zotikos.m4u.databinding.FragmentPostListBinding
import com.zotikos.m4u.di.module.ViewModelFactory
import com.zotikos.m4u.ui.base.BaseFragment
import com.zotikos.m4u.ui.vo.PostUIDto
import com.zotikos.m4u.util.AppExecutors
import com.zotikos.m4u.util.autoCleared
import com.zotikos.m4u.util.databinding.FragmentDataBindingComponent
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_post_list.*
import javax.inject.Inject


class PostListFragment : BaseFragment() {

    companion object {
        const val FRAGMENT_TAG = "PostListFragment"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: PostListViewModel

    private lateinit var binding: FragmentPostListBinding

    //private lateinit var postListAdapter: PostListAdapter

    private var adapter by autoCleared<PostListAdapter>()

    private var listener: OnFragmentInteractionListener? = null

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    @Inject
    lateinit var appExecutors: AppExecutors


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_list, container, false)

        sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(R.transition.move)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this@PostListFragment, viewModelFactory).get(PostListViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        viewModel.loadPosts()

        /* postListAdapter =
             PostListAdapter(
                 arrayListOf(),
                 mContext
             ) { postItem: PostUIDto, heroImage: ImageView, postTitle: TextView, position: Int ->

                 postItemClicked(
                     postItem,
                     heroImage,
                     postTitle,
                     position
                 )
             }*/
        setupRecycleView()

    }

    private fun setupRecycleView() {
        binding.postList.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        binding.postList.setEmptyView(posts_list_empty_view)
        binding.swipeRefreshItems.setOnRefreshListener { viewModel.loadPosts(true) }

        /*binding.postList.apply {
            adapter = postListAdapter
            //swapAdapter(postListAdapter,true)
           // layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
           // visibility = View.GONE
        }*/


        val adapter = PostListAdapter(dataBindingComponent, appExecutors) { postItem, heroImage ->
            val extras = FragmentNavigatorExtras(
                heroImage to getString(R.string.hero_image_transition)
                /*,
                postTitle to getString(R.string.title_transition)*/
            )
            navController().navigate(
                PostListFragmentDirections.openNotificationDetails(postItem),
                extras
            )
        }

        //this.postListAdapter = adapter
        this.adapter = adapter
        binding.postList.adapter = adapter

        binding.postsListEmptyView.visibility = View.GONE

        postponeEnterTransition()
        binding.postList.viewTreeObserver
            .addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
        observeViewModel()
    }

    private fun observeViewModel() {

        viewModel.getPosts().observe(viewLifecycleOwner, Observer { event ->
            val action: PostsListAction? = event?.getContentIfNotHandled()

            when (action) {
                is PostsListAction.PostsLoadingSuccessNew -> {
                    //  postListAdapter.replaceData(action.result, action.newPosts)
                    stopSwipeRefresh()

                    if (action.newPosts.isNotEmpty()) {
                        adapter.submitList(action.newPosts)
                    } else {
                        adapter.submitList(emptyList())
                    }

                }

                /* is PostsListAction.PostsLoadingSuccess -> {


                     val diffResult = DiffUtil.calculateDiff(PostItemDiffCallback(action.newPosts, action.oldPosts))

                     postListAdapter.updatePostList(action.oldPosts)
                     viewModel.oldDataList.clear()
                     viewModel.oldDataList.addAll(action.newPosts)
                     diffResult.dispatchUpdatesTo(postListAdapter)

                     binding.postList.visibility = View.VISIBLE


                     *//*  binding.postList.visibility = View.VISIBLE
                      postListAdapter.updatePostList(action.posts)*//*
                    stopSwipeRefresh()
                }*/

            }
        })

        subscribeCommonAction(viewModel.commonViewActionEvent/*, viewModel.loadingIndicator*/)
    }

    override fun handleExtraActionWhenNetworkError() {
        stopSwipeRefresh()
    }

    private fun stopSwipeRefresh() {
        if (binding.swipeRefreshItems.isRefreshing) {
            binding.swipeRefreshItems.isRefreshing = false
        }
    }

    override fun onAttach(context: Context) {
        //with out this error : late init property viewModelFactory has not been initialized
        AndroidSupportInjection.inject(this)
        if (context is OnFragmentInteractionListener) {
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
        return R.layout.fragment_post_list
    }


    private fun postItemClicked(postItem: PostUIDto, heroImage: ImageView, postTitle: TextView, position: Int) {
        Toast.makeText(activity, "Clicked: ${postItem.title}", Toast.LENGTH_LONG).show()


        val extras = FragmentNavigatorExtras(
            heroImage to getString(R.string.hero_image_transition)
            /*,
            postTitle to getString(R.string.title_transition)*/
        )
        navController().navigate(
            PostListFragmentDirections.openNotificationDetails(postItem),
            extras
        )

        /* val extras = FragmentNavigator.Extras.Builder()
             .addSharedElement(heroImage, getString(R.string.hero_image_transition))
             //.addSharedElement(postTitle, getString(R.string.title_transition))
             .build()*/

        //val action = PostListFragmentDirections.openNotificationDetails(postItem)
        //val navController = Navigation.findNavController(requireActivity(), com.zotikos.m4u.R.id.nav_fragment)
        //navController.navigate(action, extras)

        //NavHostFragment.findNavController(this@PostListFragment).navigate(action, extras)

        //listener?.showPostDetail(postItem)
    }

    interface OnFragmentInteractionListener {
        fun showPostDetail(post: PostUIDto)
    }

    /**
     * Created to be able to override in tests
     */
    fun navController() = findNavController()
}
