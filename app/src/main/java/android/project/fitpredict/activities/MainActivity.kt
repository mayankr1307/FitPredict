package android.project.fitpredict.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.project.fitpredict.R
import android.project.fitpredict.authentication.FirebaseAuth
import android.project.fitpredict.databinding.ActivityInfoBinding
import android.project.fitpredict.databinding.ActivityMainBinding
import android.project.fitpredict.firestore.Firestore
import android.project.fitpredict.models.User
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.GravityCompat

class MainActivity : BaseActivity() {

    private var binding: ActivityMainBinding? = null
    var mUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        window.statusBarColor = resources.getColor(R.color.variant2, null)

        binding?.dlMain?.tbMain?.ivUser?.setOnClickListener {
            binding?.dlMain?.dlDrawer?.openDrawer(GravityCompat.START)
        }

        binding?.dlMain?.navView?.setNavigationItemSelectedListener { menuItem ->
            onClickMenu(menuItem)
        }

        setupUserDetails()
    }

    private fun setupUserDetails() {
        Firestore().currentUserInfo(this@MainActivity, FirebaseAuth().currentUID())

        if(mUser != null) {
            binding?.dlMain?.tbMain?.tvUsername?.text = mUser!!.name
        }

        Toast.makeText(
            this@MainActivity,
            mUser?.name.toString(),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun onClickMenu(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.nav_profile -> {
                Toast.makeText(this, "Coming soon...", Toast.LENGTH_SHORT).show()
            }

            R.id.nav_sign_out -> {
                displayProgressBar(this)
                FirebaseAuth().signOutUser(this)
            }
        }
        // Close the drawer after an item is clicked
        binding?.dlMain?.dlDrawer?.closeDrawer(GravityCompat.START)
        return true
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

    fun signOutSuccess() {
        hideProgressBar()
        Toast.makeText(
            this@MainActivity,
            "Signed out successfully.",
            Toast.LENGTH_SHORT
        ).show()
        startActivity(Intent(this@MainActivity, IntroActivity::class.java))
        finish()
    }
}