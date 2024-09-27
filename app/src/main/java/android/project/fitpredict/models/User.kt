package android.project.fitpredict.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val gender: String = "",
    val weight: Double = 0.0,
    val height: Double = 0.0,
    val age: Int = 0
) : Parcelable


