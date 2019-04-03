package com.zotikos.m4u.util.exceptions

import androidx.fragment.app.FragmentActivity
import com.zotikos.m4u.R
import com.zotikos.m4u.data.model.error.ApiErrorResponse
import com.zotikos.m4u.ui.base.ErrorType
import com.zotikos.m4u.util.extension.showAlert
import java.net.HttpURLConnection

class ErrorHandlingDelegate(
    val activity: FragmentActivity?,
    private val unAuthorisedErrorAction: () -> Unit,
    private val networkUnAvailableAction: () -> Unit,
    private val unhandledErrorAction: () -> Unit
) : ErrorHandler {


    override fun handleApplicationError(
        httpStatusCode: Int,
        errorResponse: ApiErrorResponse?,
        requestId: Int,
        afterAction: () -> Unit
    ) {
        when (httpStatusCode) {

            HttpURLConnection.HTTP_UNAUTHORIZED -> {
                unAuthorisedErrorAction()
            }
            HttpURLConnection.HTTP_NOT_FOUND -> {
                errorResponse?.let {
                    if (it.errorCode == "15000") {

                    } else {
                        showMessage(errorResponse, requestId, httpStatusCode, afterAction)
                    }
                } ?: showMessage(errorResponse, requestId, httpStatusCode, afterAction)
            }
            else -> {

                showMessage(errorResponse, requestId, httpStatusCode, afterAction)

            }
        }
    }

    private fun showError(message: String, listenerAction: () -> Unit) {

        activity?.showAlert(
            message = message,
            positiveBtnText = activity.getString(R.string.label_ok),
            positiveListenerAction = { listenerAction() },
            negativeBtnText = "",
            negativeListenerAction = {},
            dismissListenerAction = listenerAction,
            cancellable = true
        )
    }

    private fun showMessage(
        errorResponse: ApiErrorResponse?,
        requestId: Int,
        httpStatusCode: Int,
        afterAction: () -> Unit
    ) {
        val msg = errorResponse?.description
        if (msg.isNullOrBlank()) activity?.getString(R.string.error_unhandled)

        activity?.showAlert(
            message = activity.getString(R.string.error_unhandled),
            positiveBtnText = activity.getString(R.string.label_ok),
            positiveListenerAction = { afterAction() },
            negativeBtnText = "",
            negativeListenerAction = {},
            dismissListenerAction = { afterAction() },
            cancellable = false
        )
    }

    override fun handleNonApplicationError(errorType: ErrorType) {
        when (errorType) {
            ErrorType.NETWORK,
            ErrorType.NO_NETWORK -> {
                networkUnAvailableAction()
            }
            else -> {
                activity?.showAlert(
                    message = activity.getString(R.string.error_unhandled),
                    positiveBtnText = activity.getString(R.string.label_ok),
                    positiveListenerAction = {
                        unhandledErrorAction()
                    },
                    negativeBtnText = "",
                    negativeListenerAction = {},
                    dismissListenerAction = {},
                    cancellable = false
                )
            }
        }
    }
}