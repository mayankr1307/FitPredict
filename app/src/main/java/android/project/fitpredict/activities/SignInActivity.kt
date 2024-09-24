package android.project.fitpredict.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.project.fitpredict.R
import android.project.fitpredict.authentication.FirebaseAuth
import android.project.fitpredict.databinding.ActivitySignInBinding
import android.widget.Toast

class SignInActivity : BaseActivity() {

    private var binding: ActivitySignInBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.ivBack?.setOnClickListener {
            onBackPressed()
        }
        binding?.btnSignIn?.setOnClickListener {
            if(validateForm()) {
                displayProgressBar(this@SignInActivity)

                val email = binding?.etEmail?.text.toString()
                val password = binding?.etPassword?.text.toString()

                FirebaseAuth().signInUser(this@SignInActivity, email, password)
            }
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true

        if(binding?.etEmail?.text?.isEmpty() == true)    isValid = false
        else if(binding?.etPassword?.text?.isEmpty() == true)    isValid = false

        if(!isValid) {
            Toast.makeText(
                this@SignInActivity,
                "Field cannot be left empty.",
                Toast.LENGTH_SHORT
            ).show()
        }

        return isValid
    }

    fun signInSuccess() {
        hideProgressBar()
        Toast.makeText(
            this@SignInActivity,
            "Sign in success.",
            Toast.LENGTH_SHORT
        ).show()
        startActivity(Intent(this@SignInActivity, MainActivity::class.java))
        finish()
    }

    fun signInFailure() {
        hideProgressBar()
        Toast.makeText(
            this@SignInActivity,
            "Sign in failure.",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}