package android.project.fitpredict.activities

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.project.fitpredict.R
import android.project.fitpredict.authentication.FirebaseAuth
import android.project.fitpredict.constants.Constants
import android.project.fitpredict.databinding.ActivitySignUpBinding
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import com.google.firestore.v1.StructuredAggregationQuery.Aggregation.Count

class SignUpActivity :  BaseActivity(), OnClickListener {

    private var binding: ActivitySignUpBinding? = null
    private var mName = ""
    private var mEmail = ""
    private var mPassword = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnSignUp?.setOnClickListener(this@SignUpActivity)
        binding?.ivBack?.setOnClickListener(this@SignUpActivity)
    }

    private fun validateForm(): Boolean {
        var isValid = true

        if(binding?.etName?.text?.isEmpty() == true)    isValid = false
        else if(binding?.etEmail?.text?.isEmpty() == true)    isValid = false
        else if(binding?.etPassword?.text?.isEmpty() == true)    isValid = false

        if(!isValid) {
            Toast.makeText(
                this@SignUpActivity,
                "Field cannot be left empty.",
                Toast.LENGTH_SHORT
                ).show()
        }

        return isValid
    }

    fun signUpSuccess() {
        hideProgressBar()
        Toast.makeText(
            applicationContext,
            "Sign up complete.",
            Toast.LENGTH_SHORT
        ).show()
        val intent = Intent(this@SignUpActivity, InfoActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun signUpFailure() {
        hideProgressBar()
        Toast.makeText(
            applicationContext,
            "Sign up failure.",
            Toast.LENGTH_SHORT
        ).show()
    }
    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.btn_sign_up -> {
                if(validateForm()) {
                    mName = binding?.etName?.text.toString()
                    mEmail = binding?.etEmail?.text.toString()
                    mPassword = binding?.etPassword?.text.toString()

                    displayProgressBar(this@SignUpActivity)
                    FirebaseAuth().signUpUser(this@SignUpActivity, mName, mEmail, mPassword)
                }
            }
            R.id.iv_back -> onBackPressed()
        }
    }
}