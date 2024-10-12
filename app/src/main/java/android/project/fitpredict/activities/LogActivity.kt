package android.project.fitpredict.activities

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.project.fitpredict.R
import android.project.fitpredict.constants.Constants
import android.project.fitpredict.databinding.ActivityLogBinding
import android.project.fitpredict.firestore.Firestore
import android.project.fitpredict.models.FoodItem
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class LogActivity : BaseActivity() {

    private var binding: ActivityLogBinding? = null
    private lateinit var mFoodItem: FoodItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        window.statusBarColor = resources.getColor(R.color.variant2, null)

        setupActivity()

        binding?.ivBack?.setOnClickListener {
            onBackPressed()
        }

        // Set up a listener for when the user changes the unit in the EditText
        binding?.etUnit?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // When the text changes, update the information
                if (!s.isNullOrEmpty()) {
                    val newServingQty = s.toString().toFloatOrNull()
                    if (newServingQty != null) {
                        updateInformation(newServingQty)
                    }
                }
            }
        })

        binding?.btnAddFood?.setOnClickListener {
            displayProgressBar(this@LogActivity)
            Firestore().storeFood(this@LogActivity, convertFoodItemToFood(mFoodItem))
        }
    }

    private fun setupActivity() {
        mFoodItem = intent.getParcelableExtra(Constants.FOOD)!!

        val foodImage: ImageView = findViewById(R.id.iv_food)
        val foodName: TextView = findViewById(R.id.tvFoodName)
        val foodCalories: TextView = findViewById(R.id.tvFoodCalories)
        val foodServingQty: TextView = findViewById(R.id.tvFoodServingQty)
        val foodServingUnit: TextView = findViewById(R.id.tvFoodServingUnit)

        val imageUrl = mFoodItem.photo?.thumb
        if (imageUrl != null) {
            Glide.with(this@LogActivity)
                .load(imageUrl)
                .placeholder(R.drawable.ic_food_placeholder) // Optional placeholder image
                .into(foodImage)
        } else {
            foodImage.setImageResource(R.drawable.ic_food_placeholder) // Default image if URL is null
        }

        foodName.text = mFoodItem.food_name
        foodCalories.text = "%.2f".format(mFoodItem.nf_calories)  // Format calories to 2 decimal places
        foodServingQty.text = mFoodItem.serving_qty.toString()
        foodServingUnit.text = mFoodItem.serving_unit

        binding?.etUnit?.setText(mFoodItem.serving_qty.toString())
        binding?.tvUnit?.text = mFoodItem.serving_unit
    }


    private fun updateInformation(newServingQty: Float) {
        val originalServingQty = mFoodItem.serving_qty
        val factor = newServingQty / originalServingQty
        val updatedCalories = mFoodItem.nf_calories * factor

        mFoodItem.serving_qty = newServingQty.toDouble()
        mFoodItem.nf_calories = updatedCalories

        // Format calories to 2 decimal places
        binding?.tvFoodCalories?.text = "%.2f".format(updatedCalories)
        binding?.tvFoodServingQty?.text = newServingQty.toString()
    }


    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    fun storeFoodSuccess() {
        hideProgressBar()
        startActivity(Intent(this@LogActivity, MainActivity::class.java))
        finish()
    }

    fun storeFoodFailure() {
        hideProgressBar()
        startActivity(Intent(this@LogActivity, MainActivity::class.java))
        finish()
    }
}
