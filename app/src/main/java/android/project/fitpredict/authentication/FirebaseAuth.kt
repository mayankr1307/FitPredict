package android.project.fitpredict.authentication

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.project.fitpredict.activities.SignInActivity
import android.project.fitpredict.activities.SignUpActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

open class FirebaseAuth {
    private val auth = Firebase.auth

    fun signUpUser(context: Context, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(context as Activity) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    if(context is SignUpActivity)   context.signUpSuccess()
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    if(context is SignUpActivity)   context.signUpFailure()
                }
            }

    }

    fun signInUser(context: Context, email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(context as Activity) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser

                    if(context is SignInActivity) {
                        context.signInSuccess()
                    }
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()

                    if(context is SignInActivity) {
                        context.signInFailure()
                    }
                }
            }
    }

    fun currentUserName(): String {
        return auth.currentUser?.displayName.toString()
    }

    fun currentUserEmail(): String {
        return auth.currentUser?.email.toString()
    }
}

