package com.zotikos.m4u.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.zotikos.m4u.R
import com.zotikos.m4u.data.model.error.ApiErrorResponse
import com.zotikos.m4u.ui.vo.Event
import com.zotikos.m4u.util.exceptions.ErrorHandlingDelegate
import com.zotikos.m4u.util.extension.showAlert
import kotlinx.android.synthetic.main.fragment_post_list.*


abstract class BaseFragment : Fragment() {

    @LayoutRes
    protected abstract fun layoutRes(): Int

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutRes(), container, false)
    }

    // the live data for common event is supposed to be provided by view model
    // components inside the view model can post event to the live data channel
    open fun subscribeCommonAction(
        liveEvent: LiveData<Event<CommonViewAction>>
        /* showLoadingIndicator: LiveData<Boolean>? = null*/
    ) {
        handleExtraActionWhenNetworkError()
        liveEvent.observe(this, Observer<Event<CommonViewAction>> { event ->
            val action: CommonViewAction? = event?.getContentIfNotHandled()
            when (action) {
                is CommonViewAction.ApplicationError -> with(action) {
                    handleApplicationError(
                        httpResponseCode,
                        errorResponse,
                        requestId,
                        afterAction
                    )
                }

                is CommonViewAction.NonApplicationError -> with(action) {
                    handleNonApplicationError(type)
                }


                is CommonViewAction.ShowError -> with(action) {
                    showError(message, listenerAction)
                }

                is CommonViewAction.ShowGenericError -> with(action) {
                    showError(listenerAction)
                }

                is CommonViewAction.ShowNetworkError -> with(action) {
                    showNetworkError(listenerAction)
                }

                is CommonViewAction.SetLoadingIndicator -> setLoadingIndicator(action.show)
            }
        })

        /* showLoadingIndicator?.observe(this, Observer<Boolean> { show ->
             show?.run {
                 setLoadingIndicator(show)
             }
         })*/
    }

    open fun handleExtraActionWhenNetworkError() {

    }
    // Override this method to handle the http exceptions
    // returned from the service calls
    open fun onError(code: Int?, message: String) {
        showError(message) { }
    }

    // Handles the network connection errors.

    open fun showNetworkError(listenerAction: () -> Unit) {
        //  showError(getString(R.string.no_network)) { listenerAction() }
        Snackbar.make(coordinatorLayout, getString(R.string.no_network), Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.retry)) { listenerAction() }
            .show()
    }

    open fun showError(listenerAction: () -> Unit) {
        showError(getString(R.string.error_unhandled)) { listenerAction() }
    }

    open fun showError(message: String, listenerAction: () -> Unit) {
        activity?.showAlert(
            message = message,
            positiveBtnText = getString(R.string.label_ok),
            positiveListenerAction = { listenerAction() },
            negativeBtnText = "",
            negativeListenerAction = {},
            dismissListenerAction = listenerAction,
            cancellable = true
        )
    }


    private val defaultErrorHandlerDelegate by lazy {
        ErrorHandlingDelegate(
            activity = activity,
            unAuthorisedErrorAction = {},
            networkUnAvailableAction = {
                showNetworkError {}
            },
            unhandledErrorAction = {
                activity?.finish()
            }
        )
    }

    protected fun handleApplicationError(
        httpStatusCode: Int,
        errorResponse: ApiErrorResponse?,
        requestId: Int,
        afterAction: () -> Unit
    ) {
        setLoadingIndicator(false)
        defaultErrorHandlerDelegate.handleApplicationError(httpStatusCode, errorResponse, requestId, afterAction)
    }

    protected fun handleNonApplicationError(errorType: ErrorType) {
        setLoadingIndicator(false)
        defaultErrorHandlerDelegate.handleNonApplicationError(errorType)
    }

    open fun setLoadingIndicator(show: Boolean) {
        /* if (show) {
             loadingView.showLoadingView()
         } else {
             loadingView.hideLoadingView()
         }*/
    }
}