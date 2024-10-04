package android.project.fitpredict.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.project.fitpredict.R
import android.project.fitpredict.adapters.SearchFoodAdapter
import android.project.fitpredict.databinding.ActivitySearchBinding
import android.project.fitpredict.models.FoodItem
import android.project.fitpredict.nutritionix.RetrofitInstance
import android.util.Log
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


        binding?.svMain?.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener,
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    searchFood(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchFood(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Make API call
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
