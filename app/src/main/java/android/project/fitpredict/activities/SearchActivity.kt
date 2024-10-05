package android.project.fitpredict.activities

import android.accessibilityservice.InputMethod
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.project.fitpredict.R
import android.project.fitpredict.adapters.SearchFoodAdapter
import android.project.fitpredict.databinding.ActivitySearchBinding
import android.project.fitpredict.models.FoodItem
import android.project.fitpredict.nutritionix.RetrofitInstance
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchActivity : AppCompatActivity() {

    private var binding: ActivitySearchBinding? = null
    private val foodList = mutableListOf<FoodItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        window.statusBarColor = resources.getColor(R.color.variant2, null)

        binding?.rvSearchResult?.layoutManager = LinearLayoutManager(this)
        val adapter = SearchFoodAdapter(this@SearchActivity, foodList)
        binding?.rvSearchResult?.adapter = adapter

        binding?.ivSearch?.setOnClickListener {
            if(binding?.etSearch?.text?.isEmpty() == true) {
                Toast.makeText(
                    this@SearchActivity,
                    "Field cannot be left empty.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)

            val query = binding?.etSearch?.text.toString()

            searchFood(query)
        }

        binding?.ivBack?.setOnClickListener {
            onBackPressed()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchFood(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.searchFood(android.project.fitpredict.nutritionix.FoodQuery(query))

                if (response.isSuccessful && response.body() != null) {
                    val foods = response.body()?.foods ?: emptyList()
                    val foodResponse = response.body()
                    Log.d("API Response", foodResponse.toString()) // Log the full response
                    // Add results to foodList and update UI
                    withContext(Dispatchers.Main) {
                        foodList.clear()
                        foodList.addAll(foods)
                        binding?.rvSearchResult?.adapter?.notifyDataSetChanged()
                        logFood(foods)
                    }
                } else {
                    Log.e("SearchActivity", "API Error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("SearchActivity", "Exception: ${e.message}")
            }
        }
    }


    private fun logFood(foods: List<FoodItem>) {
        foods.forEach {
            Log.d("Food", "Logged Food: ${it.food_name} - Calories: ${it.nf_calories}")
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}
