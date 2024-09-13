package android.project.fitpredict.activities

import android.os.Bundle
import android.project.fitpredict.R
import android.project.fitpredict.databinding.ActivitySignUpBinding
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast

class SignUpActivity :  BaseActivity(), OnClickListener {

    private var binding: ActivitySignUpBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnSignUp?.setOnClickListener(this@SignUpActivity)
    }

    private fun validateForm(): Boolean {
        var isValid = true

        if(binding?.etName?.text?.isEmpty() == true)    isValid = false
        else if(binding?.etName?.text?.isEmpty() == true)    isValid = false
        else if(binding?.etName?.text?.isEmpty() == true)    isValid = false

        if(!isValid) {
            Toast.makeText(
                this@SignUpActivity,
                "Field cannot be left empty.",
                Toast.LENGTH_SHORT
                ).show()
        }

        return isValid
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.btn_sign_up -> {

            }
        }
    }
}