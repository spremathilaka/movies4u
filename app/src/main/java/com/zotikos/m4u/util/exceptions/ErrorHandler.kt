package com.zotikos.m4u.util.exceptions

import com.zotikos.m4u.data.model.error.ApiErrorResponse
import com.zotikos.m4u.ui.base.ErrorType

interface ErrorHandler {

    fun handleApplicationError(
        httpStatusCode: Int,
        errorResponse: ApiErrorResponse?,
        requestId: Int,
        afterAction: () -> Unit
    )

    fun handleNonApplicationError(errorType: ErrorType)
}