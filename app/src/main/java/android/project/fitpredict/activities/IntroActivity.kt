package android.project.fitpredict.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.project.fitpredict.R
import android.project.fitpredict.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {

    private var binding: ActivityIntroBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.flSignUp?.setOnClickListener {
            startActivity(Intent(this@IntroActivity, SignUpActivity::class.java))
        }

        binding?.flSignIn?.setOnClickListener {

        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}