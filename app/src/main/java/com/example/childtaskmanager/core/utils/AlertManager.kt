package com.example.childtaskmanager.core.utils

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.example.childtaskmanager.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object AlertManager {

    fun showUnExpectedErrorAlert(
        context: Context,
        onPositiveBtnTap: (() -> Unit)? = null
    ): AlertDialog {
        return showAlert(
            context = context,
            message = context.getString(R.string.unexpected_error_long),
            positiveBtnText = context.getString(R.string.ok),
            onPositiveBtnTap = {
                it.dismiss()
                onPositiveBtnTap?.invoke()
            }
        )
    }

    fun showBadNetworkConnectionAlert(
        context: Context,
        onPositiveBtnTap: (() -> Unit)? = null
    ): AlertDialog {
        return showAlert(
            context = context,
            message = context.getString(R.string.network_error_long),
            positiveBtnText = context.getString(R.string.ok),
            onPositiveBtnTap = {
                it.dismiss()
                onPositiveBtnTap?.invoke()
            },
        )
    }


    fun showAlert(
        context: Context,
        title: String? = null,
        message: String,
        positiveBtnText: String,
        onPositiveBtnTap: (dialogInterface: DialogInterface) -> Unit,
        negativeBtnText: String? = null,
        onNegativeBtnTap: ((dialogInterface: DialogInterface) -> Unit)? = null
    ): AlertDialog {
        val builder = MaterialAlertDialogBuilder(context)
        if (!title.isNullOrEmpty()) {
            builder.setTitle(title)
        }
        builder.setMessage(message)
        builder.setPositiveButton(
            positiveBtnText
        ) { dialogInterface, _ ->
            onPositiveBtnTap(dialogInterface)
        }

        if (!negativeBtnText.isNullOrEmpty() && onNegativeBtnTap != null) {
            builder.setNegativeButton(
                negativeBtnText
            ) { dialogInterface, _ ->
                onNegativeBtnTap(dialogInterface)
            }
        }

        val alertDialog = builder.create()
        alertDialog.show()
        return alertDialog
    }

}