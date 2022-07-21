package com.example.bulletinboard.accounthelper

import android.util.Log
import android.widget.Toast
import com.example.bulletinboard.MainActivity
import com.example.bulletinboard.R
import com.example.bulletinboard.constans.FirebaseAuthConstans
import com.example.bulletinboard.dialoghelper.GoogleAccConst
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.*

class AccountHelper(act: MainActivity) {
    private val act = act
    private lateinit var signInClient: GoogleSignInClient

    fun sighUpWithEmail(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            act.mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        sendEmailVerification(task.result?.user!!)
                        act.uiUpdate(task.result?.user)

                    } else {
//                        Toast.makeText(
//                            act,
//                            act.resources.getString(R.string.sign_up_error),
//                            Toast.LENGTH_LONG
//                        ).show()
//                        Log.d("MyLog", "Exeption + ${task.exception}")


                        if (task.exception is FirebaseAuthUserCollisionException) {
                            val exception = task.exception as FirebaseAuthUserCollisionException
                            if (exception.errorCode == FirebaseAuthConstans.ERROR_EMAIL_ALREADY_IN_USE) {
                                linkEmailToG(email, password)
//                                Toast.makeText(
//                                    act,
//                                    "${FirebaseAuthConstans.ERROR_EMAIL_ALREADY_IN_USE}",
//                                    Toast.LENGTH_LONG
//                                ).show()

                            }
                        } else if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            val exception =
                                task.exception as FirebaseAuthInvalidCredentialsException
                            if (exception.errorCode == FirebaseAuthConstans.ERROR_INVALID_EMAIL) {
                                Toast.makeText(
                                    act,
                                    "${FirebaseAuthConstans.ERROR_INVALID_EMAIL}",
                                    Toast.LENGTH_LONG
                                ).show()
                                //Log.d("MyLog", "Exeption + ${exception.errorCode}")
                            } else if (task.exception is FirebaseAuthWeakPasswordException) {
                                val exception =
                                    task.exception as FirebaseAuthWeakPasswordException
//                                Log.d("MyLog", "Exeption + ${exception.errorCode}")

                                if (exception.errorCode == FirebaseAuthConstans.ERROR_WEAK_PASSWORD) {
                                    Toast.makeText(
                                        act,
                                        "${FirebaseAuthConstans.ERROR_WEAK_PASSWORD}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                        }
                    }
                }



        }

    }


    fun sighInWithEmail(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            act.mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    act.uiUpdate(task.result?.user)

                } else {
//                    Log.d("MyLog", "Exeption + ${task.exception}")
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        val exception =
                            task.exception as FirebaseAuthInvalidCredentialsException
                        if (exception.errorCode == FirebaseAuthConstans.ERROR_INVALID_EMAIL) {
                            Toast.makeText(
                                act,
                                "${FirebaseAuthConstans.ERROR_INVALID_EMAIL}",
                                Toast.LENGTH_LONG
                            ).show()

                        } else if (exception.errorCode == FirebaseAuthConstans.ERROR_WRONG_PASSWORD) {
                            Toast.makeText(
                                act,
                                "${FirebaseAuthConstans.ERROR_WRONG_PASSWORD}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else if (task.exception is FirebaseAuthInvalidUserException) {
                        val exception =
                            task.exception as FirebaseAuthInvalidUserException
//                        Log.d("MyLog", "Exeption + ${exception.errorCode}")
                        if (exception.errorCode == FirebaseAuthConstans.ERROR_USER_NOT_FOUND) {
                            Toast.makeText(
                                act,
                                "${FirebaseAuthConstans.ERROR_USER_NOT_FOUND}",
                                Toast.LENGTH_LONG
                            ).show()
                        }


                    }

                }
            }
        }
    }


        private fun sendEmailVerification(user: FirebaseUser) {
            user.sendEmailVerification().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        act,
                        act.resources.getString(R.string.send_verification_done),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        act,
                        act.resources.getString(R.string.send_verification_error),
                        Toast.LENGTH_LONG
                    ).show()

                }
            }

        }

        private fun linkEmailToG(email: String, password: String) {
            val credential = EmailAuthProvider.getCredential(email, password)
            if (act.mAuth.currentUser != null) {
                act.mAuth.currentUser?.linkWithCredential(credential)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                act,
                                act.resources.getString(R.string.link_done),
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                act,
                                act.resources.getString(R.string.enter_to_gmail),
                                Toast.LENGTH_LONG
                            ).show()

                        }

                    }
            }
        }

        private fun getSignInClient(): GoogleSignInClient {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(act.getString(R.string.default_web_client_id)).requestEmail()
                .build()
            return GoogleSignIn.getClient(act, gso)

        }


        fun signInWithGoogle() {
            signInClient = getSignInClient()
            val intent = signInClient.signInIntent
            act.startActivityForResult(intent, GoogleAccConst.GOOGLE_SIGN_REQUEST_CODE)

        }

        fun signOutG() {
            getSignInClient().signOut()

        }

        fun signInFirebaseWithGoogle(token: String) {
            val credential = GoogleAuthProvider.getCredential(token, null)
            act.mAuth.signInWithCredential(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(act, "Sign is done", Toast.LENGTH_LONG).show()
                    act.uiUpdate(task.result?.user)
                } else {
                    Log.d("MyLog", "Google sign in exeption + ${task.exception}")
                }
            }
        }
    }

