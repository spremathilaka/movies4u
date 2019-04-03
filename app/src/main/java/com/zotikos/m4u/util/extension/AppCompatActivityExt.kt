package com.zotikos.m4u.util.extension

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.zotikos.m4u.R

fun Activity.showAlert(
    title: String = "",
    message: String,
    positiveBtnText: String,
    positiveListenerAction: () -> Unit,
    negativeBtnText: String = "",
    negativeListenerAction: () -> Unit?,
    dismissListenerAction: () -> Unit,
    cancellable: Boolean = true
) {

    val builder = AlertDialog.Builder(this, R.style.DefaultAlertDialogStyle)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setCancelable(cancellable)
    /**
     * To track whether the user has actioned it, or its a dismiss
     * triggered by user clicking outside the alert dialog.
     */
    var actioned = false
    builder.setPositiveButton(positiveBtnText) { dialogInterface, _ ->
        actioned = true
        dialogInterface.dismiss()
        positiveListenerAction()
    }

    if (negativeBtnText.isNotEmpty()) {
        builder.setNegativeButton(negativeBtnText) { dialogInterface, _ ->
            actioned = true
            dialogInterface.dismiss()
            negativeListenerAction()
        }
    }

    builder.setOnDismissListener { if (!actioned) dismissListenerAction() }
    if (!isFinishing) {
        builder.show()
    }
}