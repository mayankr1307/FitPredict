package android.project.fitpredict.firestore

import android.content.ContentValues.TAG
import android.content.Context
import android.project.fitpredict.activities.InfoActivity
import android.project.fitpredict.activities.LogActivity
import android.project.fitpredict.activities.MainActivity
import android.project.fitpredict.models.Food
import android.project.fitpredict.models.User
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.log

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

    fun storeFood(logActivity: LogActivity, food: Food) {
        val foodRef = db.collection("food").document(food.loggedBy + food.loggedTime)

        val foodMap = mapOf(
            "foodName" to food.foodName,
            "quantity" to food.quantity,
            "unit" to food.unit,
            "calories" to food.calories,
            "protein" to food.protein,
            "carbs" to food.carbs,
            "fats" to food.fats,
            "loggedBy" to food.loggedBy,
            "loggedDate" to food.loggedDate,
            "loggedTime" to food.loggedTime
        )

        foodRef.set(foodMap)
            .addOnSuccessListener {
                Log.d(TAG, "Food item added for user ${food.loggedBy}")
                Toast.makeText(logActivity, "Food logged successfully.", Toast.LENGTH_SHORT).show()
                logActivity.storeFoodSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error adding food", e)
                Toast.makeText(logActivity, "Failed to log food. Please try again.", Toast.LENGTH_SHORT).show()
                logActivity.storeFoodFailure()
            }
    }

    fun deleteFoodItem(userId: String, loggedTime: String, callback: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val documentId = userId + loggedTime

        db.collection("food").document(documentId)
            .delete()
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }


    fun retrieveFood(mainActivity: MainActivity, uid: String, date: String) {
        val foodRef = db.collection("food")
            .whereEqualTo("loggedBy", uid)
            .whereEqualTo("loggedDate", date)

        foodRef.get()
            .addOnSuccessListener { documents ->
                val foodList = ArrayList<Food>()
                if (documents != null && !documents.isEmpty) {
                    for (document in documents) {
                        val foodItem = document.toObject(Food::class.java)
                        foodList.add(foodItem)
                    }

                } else {
                    Log.d(TAG, "No food logs found for user $uid on $date")
                }

                mainActivity.foodRetrievedSuccessfully(foodList)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error retrieving food logs", e)
                Toast.makeText(mainActivity, "Failed to retrieve food logs. Please try again.", Toast.LENGTH_SHORT).show()
                mainActivity.foodRetrievedFailure()
            }
    }


}
