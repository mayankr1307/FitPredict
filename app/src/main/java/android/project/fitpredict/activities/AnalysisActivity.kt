package android.project.fitpredict.activities

import android.content.Intent
import android.os.Bundle
import android.project.fitpredict.R
import android.project.fitpredict.authentication.FirebaseAuth
import android.project.fitpredict.databinding.ActivityAnalysisBinding
import android.project.fitpredict.firestore.Firestore
import android.project.fitpredict.models.Food
import android.project.fitpredict.models.User
import android.project.fitpredict.models.UserMetric
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast

class AnalysisActivity : BaseActivity() {

    private var binding: ActivityAnalysisBinding? = null

    lateinit var mUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalysisBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        window.statusBarColor = resources.getColor(R.color.variant7, null)

        displayProgressBar(this@AnalysisActivity)
        Firestore().currentUserInfo(this@AnalysisActivity, FirebaseAuth().currentUID())

        binding?.btnAnalyze?.setOnClickListener {
            // Get the selected activity level from RadioGroup
            val selectedActivityButtonId = binding?.rgActivityLevel?.checkedRadioButtonId
            Log.e("Selected Activity:", selectedActivityButtonId.toString())
            if(selectedActivityButtonId == -1) {
                Toast.makeText(this, "Please select an activity level.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val activityLevel = findViewById<RadioButton>(selectedActivityButtonId!!)?.text.toString()

            Log.e("Selected Activity Level:", activityLevel)

            if (activityLevel.isNullOrEmpty() || activityLevel == "Select Activity Level") {
                Toast.makeText(this, "Please select an activity level.", Toast.LENGTH_SHORT).show()
            } else {
                // Calculate total values for calories, protein, carbs, and fats
                val totalCalories = binding?.tvCalories?.text.toString()
                val totalProtein = binding?.tvProtein?.text.toString()
                val totalCarbs = binding?.tvCarbs?.text.toString()
                val totalFats = binding?.tvFats?.text.toString()

                // Create a UserMetric object with all the necessary information
                val userMetric = UserMetric(
                    age = mUser.age.toString(),
                    gender = mUser.gender,
                    height = mUser.height.toString(),
                    weight = mUser.weight.toInt().toString(),
                    activityLevel = activityLevel,
                    totalCalories = totalCalories,
                    totalProtein = totalProtein,
                    totalCarbs = totalCarbs,
                    totalFats = totalFats
                )

                // Create an intent and pass the UserMetric object to ResultActivity
                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra("userMetric", userMetric)
                startActivity(intent)
            }
        }


    }

    fun updateUserInfo() {
        binding?.apply {
            tvAge.text = mUser.age.toString()
            tvGender.text = mUser.gender
            tvHeight.text = "${mUser.height} cm"
            tvWeight.text = "${mUser.weight} kg"
        }

        fetchFoodData()
    }

    private fun fetchFoodData() {
        val currentUID = FirebaseAuth().currentUID()

        Firestore().fetchFoodLoggedToday(
            uid = currentUID,
            callback = { foodList ->
                updateFoodData(foodList)
                hideProgressBar()
            },
            onFailure = { e ->
                hideProgressBar()
                Toast.makeText(this, "Error fetching food data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun updateFoodData(foodList: List<Food>) {
        var totalCalories = 0.0
        var totalProtein = 0.0
        var totalCarbs = 0.0
        var totalFats = 0.0

        for (food in foodList) {
            totalCalories += food.calories
            totalProtein += food.protein
            totalCarbs += food.carbs
            totalFats += food.fats
        }

        binding?.apply {
            tvCalories.text = String.format("%.2f kcal", totalCalories)
            tvProtein.text = String.format("%.2f g", totalProtein)
            tvCarbs.text = String.format("%.2f g", totalCarbs)
            tvFats.text = String.format("%.2f g", totalFats)
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}
