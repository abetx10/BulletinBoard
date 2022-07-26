package com.example.bulletinboard.dialoghelper

import android.app.Activity
import android.app.AlertDialog
import com.example.bulletinboard.databinding.ProgressDialogLayoutBinding


object ProgressDialog {

    fun createProgressDialog(act : Activity) : AlertDialog {
        val builder = AlertDialog.Builder(act)
        val bindingDialog = ProgressDialogLayoutBinding.inflate(act.layoutInflater)
        val view = bindingDialog.root
        builder.setView(view)
        val dialog = builder.create()
        dialog.setCancelable(false)
        dialog.show()
        return dialog
    }
}