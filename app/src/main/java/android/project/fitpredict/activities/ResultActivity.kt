package android.project.fitpredict.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.project.fitpredict.R
import android.project.fitpredict.databinding.ActivityResultBinding
import android.project.fitpredict.firestore.Firestore
import android.project.fitpredict.models.UserMetric

class ResultActivity : BaseActivity() {

    private var binding: ActivityResultBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        displayProgressBar(this@ResultActivity)
        Firestore().analyze(this@ResultActivity)
    }

    fun obtainResults() {
        val userMetric = intent.getParcelableExtra<UserMetric>("userMetric")

        if (userMetric != null) {
            // Convert the necessary fields from String to Double/Int
            val age = userMetric.age.toIntOrNull()
            val height = userMetric.height.toDoubleOrNull()
            val weight = userMetric.weight.toDoubleOrNull()

            // Remove 'kcal' from the totalCalories string and convert it to a Double
            val totalCaloriesString =
                userMetric.totalCalories.replace(" kcal", "").replace(",", "").trim()
            val totalCalories = totalCaloriesString.toDoubleOrNull()

            // Check if conversion was successful
            if (age != null && height != null && weight != null && totalCalories != null) {
                val bmr = calculateBMR(userMetric, weight, height, age)
                val activityMultiplier = getActivityMultiplier(userMetric.activityLevel)
                val maintenanceCalories = bmr * activityMultiplier

                // Calculate the final result based on the input calories
                val finalResult = calculateFinalResult(maintenanceCalories, totalCalories)

                // Set the image and text based on the final result
                updateUI(finalResult)

                // Handle button click to go back to MainActivity
                binding?.btnBack?.setOnClickListener {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Optionally finish the current activity
                }
            } else {
                Toast.makeText(this, "Invalid input data", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "No user information available", Toast.LENGTH_SHORT).show()
        }
    }

    private fun calculateBMR(userMetric: UserMetric, weight: Double, height: Double, age: Int): Double {
        return if (userMetric.gender == "Male") {
            10 * weight + 6.25 * height - 5 * age + 5
        } else {
            10 * weight + 6.25 * height - 5 * age - 161
        }
    }

    private fun getActivityMultiplier(activityLevel: String): Double {
        return when (activityLevel) {
            "Sedentary (little or no exercise)" -> 1.2
            "Lightly Active (light exercise/sports 1-3 days/week)" -> 1.375
            "Moderately Active (moderate exercise/sports 3-5 days/week)" -> 1.55
            "Very Active (hard exercise/sports 6-7 days a week)" -> 1.725
            "Super Active (very hard exercise, physical job, or training twice a day)" -> 1.9
            else -> 1.2
        }
    }

    private fun calculateFinalResult(maintenanceCalories: Double, inputCalories: Double): Int {
        val difference = inputCalories - maintenanceCalories
        val percentageDifference = (difference / maintenanceCalories) * 100

        return when {
            percentageDifference in -5.0..5.0 -> 0 // Within 5% of maintenance calories (fit)
            percentageDifference > 10 -> 2 // More than 10% over maintenance calories (fat)
            percentageDifference > 5 -> 1 // Over by 10% or less (overweight)
            percentageDifference < -10 -> -2 // More than 10% under maintenance calories (underweight)
            else -> -1 // Under by 10% or less (skinny)
        }
    }

    private fun updateUI(result: Int) {
        hideProgressBar()
        when (result) {
            -2 -> {
                binding?.ivResult?.setImageResource(R.drawable.ic_underweight)
                binding?.tvResult?.text = "Prediction: Lose weight\nOops! Looks like you're under-eating, try a snack!"
            }
            -1 -> {
                binding?.ivResult?.setImageResource(R.drawable.ic_skinny)
                binding?.tvResult?.text = "Prediction: Lose weight\nOops! You're looking a bit too lean, maybe add some extra meals!"
            }
            0 -> {
                binding?.ivResult?.setImageResource(R.drawable.ic_fit)
                binding?.tvResult?.text = "Prediction: Maintain weight\nYou're doing great! Keep it up!"
            }
            1 -> {
                binding?.ivResult?.setImageResource(R.drawable.ic_overweight)
                binding?.tvResult?.text = "Prediction: Gain weight\nUh-oh! Looks like you're indulging a bit too much, maybe ease off those snacks!"
            }
            2 -> {
                binding?.ivResult?.setImageResource(R.drawable.ic_fat)
                binding?.tvResult?.text = "Prediction: Gain weight\nWhoa there! Looks like it's time to hit the gym and cut back on those calories!"
            }
        }
    }


    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}