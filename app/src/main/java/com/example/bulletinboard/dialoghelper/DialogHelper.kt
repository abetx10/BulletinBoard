package com.example.bulletinboard.dialoghelper

import android.app.AlertDialog
import android.view.View
import android.widget.Toast
import com.example.bulletinboard.MainActivity
import com.example.bulletinboard.R
import com.example.bulletinboard.accounthelper.AccountHelper
import com.example.bulletinboard.databinding.SignDialogBinding

class DialogHelper(act: MainActivity) {
    private val act = act
    val accHelper = AccountHelper(act)
    fun createSignDialog(index: Int) {
        val builder = AlertDialog.Builder(act)
        val bindingDialog = SignDialogBinding.inflate(act.layoutInflater)
        val view = bindingDialog.root
        builder.setView(view)
        setDialogState(index, bindingDialog)


        val dialog = builder.create()
        bindingDialog.btSignUpIN.setOnClickListener {
            setOnClickSignUpIn(index, bindingDialog, dialog)
        }
        bindingDialog.btForgetPassword.setOnClickListener {
            setOnClickResetPassword(bindingDialog, dialog)
            }
        bindingDialog.btGoogleSignIn.setOnClickListener {
            accHelper.signInWithGoogle()
            dialog.dismiss()

        }
        dialog.show()
    }

    private fun setOnClickResetPassword(bindingDialog: SignDialogBinding, dialog: AlertDialog?) {
        if (bindingDialog.edSignEmail.text.isNotEmpty()){
            act.mAuth.sendPasswordResetEmail(bindingDialog.edSignEmail.text.toString()).addOnCompleteListener  {task ->
                if (task.isSuccessful){
                    Toast.makeText(act, R.string.email_reset_email, Toast.LENGTH_LONG).show()

                }
            }
            dialog?.dismiss()
        }else {
            bindingDialog.tvDialogMessage.visibility = View.VISIBLE

        }

    }

    private fun setOnClickSignUpIn(
        index: Int,
        bindingDialog: SignDialogBinding,
        dialog: AlertDialog?
    ) {
        dialog?.dismiss()
        if (index == DialogConst.SIGN_UP_STATE) {
            accHelper.sighUpWithEmail(
                bindingDialog.edSignEmail.text.toString(),
                bindingDialog.edSignPassword.text.toString()
            )

        } else {
            accHelper.sighInWithEmail(
                bindingDialog.edSignEmail.text.toString(),
                bindingDialog.edSignPassword.text.toString()
            )

        }

    }

    private fun setDialogState(index: Int, bindingDialog: SignDialogBinding) {
        if (index == DialogConst.SIGN_UP_STATE) {
            bindingDialog.tvSignTitle.text = act.resources.getString(R.string.ac_sigh_up)
            bindingDialog.btSignUpIN.text = act.resources.getString(R.string.sign_up_action)
        } else {
            bindingDialog.tvSignTitle.text = act.resources.getString(R.string.ac_sigh_in)
            bindingDialog.btSignUpIN.text = act.resources.getString(R.string.sign_in_action)
            bindingDialog.btForgetPassword.visibility = View.VISIBLE
        }

    }
}