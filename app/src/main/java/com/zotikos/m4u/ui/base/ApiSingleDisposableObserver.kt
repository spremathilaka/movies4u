package com.zotikos.m4u.ui.base

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.zotikos.m4u.data.model.error.ApiErrorResponse
import com.zotikos.m4u.ui.vo.Event
import com.zotikos.m4u.util.exceptions.OfflineException
import io.reactivex.observers.DisposableSingleObserver
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.security.cert.CertPathValidatorException


abstract class ApiSingleDisposableObserver<T>(
    private val requestId: Int,
    private val liveEvent: MutableLiveData<Event<CommonViewAction>>? = null,
    private val showLoadingIndicator: MutableLiveData<Boolean>? = null,
    private val retryAction: () -> Unit
) : DisposableSingleObserver<T>() {

    override fun onError(e: Throwable) {
        Timber.e("Error processing the response $e")

        val type: ErrorType

        when (e) {
            is HttpException -> {

                val httpResponse: HttpException = e
                var errorResponse: ApiErrorResponse? = null
                try {
                    errorResponse = Gson().fromJson(httpResponse.response().errorBody()?.string(), getErrorClass())
                } catch (e: JsonSyntaxException) { /*to handle errors like 502 */
                }
                handleApplicationError(httpResponse.response().code(), errorResponse, requestId)
                return
            }


            /**
             * These exception could have been throw because of invalid response body like non nullable field
             * was returned as null, incorrectly formatted json etc. All these should be considered as
             * application errors. As incorrect data is as similar as no data, we are considering them
             * as 404, but this view might change. Need to revisit.
             *
             * Ideally we should allow the application to crash
             */
            is NullPointerException,
            is JsonSyntaxException,
            is IndexOutOfBoundsException -> {
                handleApplicationError(HttpURLConnection.HTTP_NOT_FOUND, requestId = requestId)
                return
            }

            /*       is SessionExpired -> {
                       handleApplicationError(HttpURLConnection.HTTP_UNAUTHORIZED, requestId = requestId)
                       return
                   }*/
            is SocketTimeoutException -> {
                type = ErrorType.NETWORK
                handleNetworkError(retryAction)
                return
            }
            is CertPathValidatorException,
            is IOException,
            is OfflineException -> {
                type = ErrorType.NO_NETWORK
                handleNetworkError(retryAction)
                return
            }
            else -> {
                type = ErrorType.OTHER
            }
        }
        handleNonApplicationError(type)
    }

    open fun handleApplicationError(httpResponseCode: Int, errorResponse: ApiErrorResponse? = null, requestId: Int) {
        showLoadingIndicator?.postValue(false)
        liveEvent?.postValue(Event(CommonViewAction.ApplicationError(httpResponseCode, errorResponse, requestId)))
    }

    open fun handleNonApplicationError(type: ErrorType) {
        showLoadingIndicator?.postValue(false)
        liveEvent?.postValue(Event(CommonViewAction.NonApplicationError(type)))
    }

    open fun handleNetworkError(retryAction: () -> Unit) {
        showLoadingIndicator?.postValue(false)
        liveEvent?.postValue(Event(CommonViewAction.ShowNetworkError(retryAction)))
    }

    override fun onSuccess(response: T) {
        showLoadingIndicator?.postValue(false)
    }

    open fun getErrorClass(): Class<out ApiErrorResponse> = ApiErrorResponse::class.java
}