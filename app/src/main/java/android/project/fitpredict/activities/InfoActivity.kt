package android.project.fitpredict.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.project.fitpredict.R
import android.project.fitpredict.authentication.FirebaseAuth
import android.project.fitpredict.databinding.ActivityInfoBinding
import android.project.fitpredict.databinding.ActivitySignUpBinding
import android.project.fitpredict.firestore.Firestore
import android.project.fitpredict.models.User
import android.widget.Toast

class InfoActivity : BaseActivity() {

    private var binding: ActivityInfoBinding? = null
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
            if(validateForm()) {
                setValues()
                val user = User(
                    mName,
                    mEmail,
                    mGender,
                    mWeight,
                    mHeight,
                    mAge
                )
                displayProgressBar(this@InfoActivity)
                Firestore().storeUserInfo(this@InfoActivity, user)
            }
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

        mName = FirebaseAuth().currentUserName()
        mEmail = FirebaseAuth().currentUserEmail()

        if(binding?.rbMale?.isActivated == true)    mGender = "Male"
        else if(binding?.rbFemale?.isActivated == true) mGender = "Female"
        else    mGender = "Other"
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}