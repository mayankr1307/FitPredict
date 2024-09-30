package android.project.fitpredict.firestore

import android.content.ContentValues.TAG
import android.content.Context
import android.project.fitpredict.activities.InfoActivity
import android.project.fitpredict.activities.MainActivity
import android.project.fitpredict.models.User
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

open class Firestore {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun storeUserInfo(context: Context, user: User) {
        val userId = user.uid
        val userRef = db.collection("users").document(userId)

        val userMap = mapOf(
            "uid" to user.uid,
            "name" to user.name,
            "email" to user.email,
            "gender" to user.gender,
            "weight" to user.weight,
            "height" to user.height,
            "age" to user.age
        )

        userRef.set(userMap)
            .addOnSuccessListener {
                if(context is InfoActivity) {
                    context.dataStorageSuccess()
                }
            }
            .addOnFailureListener { e ->
                Log.e("STORAGE_FAILURE", e.message.toString())
                if(context is InfoActivity) {
                    context.dataStorageFailure()
                }
            }
    }

    fun currentUserInfo(context: Context, uid: String) {
        val userRef = db.collection("users").document(uid)
        userRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val user = document.toObject(User::class.java)

                    if(context is MainActivity) {
                        context.mUser = user
                        Log.d(TAG, "User Info: ${context.mUser}")
                        context.userInfoRetrievedSuccessfully()
                    }
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Failed to get document", exception)
            }
    }

}
