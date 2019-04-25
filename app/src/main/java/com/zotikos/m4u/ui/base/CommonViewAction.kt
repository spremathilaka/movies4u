package com.zotikos.m4u.ui.base


import com.zotikos.m4u.data.model.error.ApiErrorResponse

/*
* Meant to be used with LiveData + Event wrapper to handle common actions with Mvvm pattern
* Need to remove some mvp legacies in the next phase
* */
open class CommonViewAction {
    data class ApplicationError(
        val httpResponseCode: Int,
        val errorResponse: ApiErrorResponse? = null,
        val requestId: Int,
        val afterAction: () -> Unit = {}
    ) : CommonViewAction()

    data class NonApplicationError(val type: ErrorType) : CommonViewAction()
    data class SetLoadingIndicator(val show: Boolean) : CommonViewAction()

    class ShowError(val message: String, val listenerAction: () -> Unit = {}) : CommonViewAction()
    class ShowGenericError(val listenerAction: () -> Unit = {}) : CommonViewAction()
    class ShowNetworkError(val listenerAction: () -> Unit = {}) : CommonViewAction()
}