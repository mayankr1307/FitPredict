package android.project.fitpredict.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Photo(
    val thumb: String,
    val highres: String?,
    val is_user_uploaded: Boolean
) : Parcelable
