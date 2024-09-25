package android.project.fitpredict.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.project.fitpredict.R
import android.project.fitpredict.databinding.ActivityInfoBinding
import android.project.fitpredict.databinding.ActivityMainBinding
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.GravityCompat

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        window.statusBarColor = resources.getColor(R.color.variant2, null)

        binding?.dlMain?.tbMain?.ivUser?.setOnClickListener {
            binding?.dlMain?.dlDrawer?.openDrawer(GravityCompat.START)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (binding?.dlMain?.dlDrawer?.isDrawerOpen(GravityCompat.START) == true) {
            super.onBackPressed()
        } else {
            binding?.dlMain?.dlDrawer?.openDrawer(GravityCompat.START)
        }
    }


    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}