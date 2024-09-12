package android.project.fitpredict.activities

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

        }

        binding?.flSignIn?.setOnClickListener {

        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}