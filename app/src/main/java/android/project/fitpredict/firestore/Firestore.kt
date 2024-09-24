package android.project.fitpredict.firestore

import android.content.Context
import android.project.fitpredict.activities.InfoActivity
import android.project.fitpredict.models.User
import android.util.Log
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
}
