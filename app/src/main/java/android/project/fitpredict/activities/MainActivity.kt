package android.project.fitpredict.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.project.fitpredict.R
import android.project.fitpredict.adapters.FoodAdapter
import android.project.fitpredict.authentication.FirebaseAuth
import android.project.fitpredict.databinding.ActivityInfoBinding
import android.project.fitpredict.databinding.ActivityMainBinding
import android.project.fitpredict.firestore.Firestore
import android.project.fitpredict.models.Food
import android.project.fitpredict.models.User
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.core.view.isEmpty
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : BaseActivity() {

    private var binding: ActivityMainBinding? = null
    private lateinit var mFoodRecyclerView: RecyclerView
    private lateinit var mFoodAdapter: FoodAdapter
    var mUser: User? = null
    var mFoodList = ArrayList<Food>()

    private var currentDate: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        window.statusBarColor = resources.getColor(R.color.variant2, null)

        mFoodRecyclerView = binding?.dlMain?.mainContent?.rvFoodLog!!

        binding?.dlMain?.tbMain?.ivUser?.setOnClickListener {
            binding?.dlMain?.dlDrawer?.openDrawer(GravityCompat.START)
        }

        binding?.dlMain?.navView?.setNavigationItemSelectedListener { menuItem ->
            onClickMenu(menuItem)
        }

        binding?.dlMain?.mainContent?.fbAdd?.setOnClickListener {
            startActivity(Intent(this@MainActivity, SearchActivity::class.java))
        }

        setupDateChangeListeners()

        loadFoodData()

        checkRVEmpty()

        setupUserDetails()

        setupCurrentDate()
    }

    private fun loadFoodData() {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        Firestore().retrieveFood(this@MainActivity, FirebaseAuth().currentUID(), sdf.format(currentDate.time))
    }

    private fun setupCurrentDate() {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        binding?.dlMain?.tvDate?.text = sdf.format(currentDate.time)
    }

    private fun setupDateChangeListeners() {
        binding?.dlMain?.ivPrevious?.setOnClickListener {
            changeDate(-1)
        }

        binding?.dlMain?.ivNext?.setOnClickListener {
            changeDate(1)
        }
    }

    private fun setupRecyclerView() {
        mFoodAdapter = FoodAdapter(this, mFoodList)

       mFoodRecyclerView?.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mFoodAdapter
        }
    }

    private fun changeDate(days: Int) {
        currentDate.add(Calendar.DAY_OF_MONTH, days)
        loadFoodData()
        setupCurrentDate()
    }

    private fun checkRVEmpty() {
        if (mFoodRecyclerView.isEmpty()) {
            binding?.dlMain?.mainContent?.tvEmpty?.visibility = View.VISIBLE
        } else {
            binding?.dlMain?.mainContent?.tvEmpty?.visibility = View.GONE
        }
    }

    private fun setupUserDetails() {
        Firestore().currentUserInfo(this@MainActivity, FirebaseAuth().currentUID())
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

    fun userInfoRetrievedSuccessfully() {
        binding?.dlMain?.navView?.getHeaderView(0)?.findViewById<TextView>(R.id.tv_username)!!.text = mUser!!.name
    }

    fun foodRetrievedSuccessfully(foodList: ArrayList<Food>) {
        mFoodList.clear()
        mFoodList.addAll(foodList)

        setupRecyclerView()
    }
}
