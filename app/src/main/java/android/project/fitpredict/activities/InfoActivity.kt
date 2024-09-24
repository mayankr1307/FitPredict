package android.project.fitpredict.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.project.fitpredict.R
import android.project.fitpredict.authentication.FirebaseAuth
import android.project.fitpredict.constants.Constants
import android.project.fitpredict.databinding.ActivityInfoBinding
import android.project.fitpredict.databinding.ActivitySignUpBinding
import android.project.fitpredict.firestore.Firestore
import android.project.fitpredict.models.User
import android.util.Log
import android.widget.Toast
import com.google.firebase.Firebase

class InfoActivity : BaseActivity() {

    private var binding: ActivityInfoBinding? = null
    private var mUID = ""
    private var mName = ""
    private var mEmail = ""
    private var mGender = ""
    private var mWeight: Double = 0.0
    private var mHeight: Double = 0.0
    private var mAge: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnSubmit?.setOnClickListener {
            submit()
        }
    }

    private fun submit() {
        if (validateForm()) {
            setValues()
            val user = User(
                mUID,
                mName,
                mEmail,
                mGender,
                mWeight,
                mHeight,
                mAge
            )

            Log.d("UserInfo", "User Information: UID: $mUID, Name: $mName, Email: $mEmail, Gender: $mGender, Weight: $mWeight, Height: $mHeight, Age: $mAge")

            displayProgressBar(this@InfoActivity)
            Firestore().storeUserInfo(this@InfoActivity, user)
        }
    }

    fun dataStorageSuccess() {
        hideProgressBar()
        Toast.makeText(
            this@InfoActivity,
            "Data stored successfully.",
            Toast.LENGTH_SHORT
        ).show()

    }

    fun dataStorageFailure() {
        hideProgressBar()
        Toast.makeText(
            this@InfoActivity,
            "Data storage was unsuccessful.",
            Toast.LENGTH_SHORT
        ).show()
    }
    private fun validateForm(): Boolean {
        var isValid = true

        if(binding?.rgGender?.checkedRadioButtonId == -1) isValid = false
        else if(binding?.etWeight?.text?.isEmpty() == true) isValid = false
        else if(binding?.etAge?.text?.isEmpty() == true) isValid = false
        else if(binding?.etHeight?.text?.isEmpty() == true) isValid = false

        if(!isValid) {
            Toast.makeText(
                this@InfoActivity,
                "Field cannot be left empty.",
                Toast.LENGTH_SHORT
            ).show()
        }

        return isValid
    }

    private fun setValues() {
        mWeight = binding?.etWeight?.text.toString().toDoubleOrNull() ?: 0.0
        mHeight = binding?.etHeight?.text.toString().toDoubleOrNull() ?: 0.0
        mAge = binding?.etAge?.text.toString().toIntOrNull() ?: 0

        mUID = FirebaseAuth().currentUID()

        mName = FirebaseAuth().currentUserDisplayName()
        mEmail = FirebaseAuth().currentUserEmail()

        if(binding?.rbMale?.isChecked == true)    mGender = "Male"
        else if(binding?.rbFemale?.isChecked == true) mGender = "Female"
        else if(binding?.rbOther?.isChecked == true)    mGender = "Other"
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}