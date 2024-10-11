package android.project.fitpredict.activities

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.project.fitpredict.R
import android.project.fitpredict.authentication.FirebaseAuth
import android.project.fitpredict.models.Food
import android.project.fitpredict.models.FoodItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

open class BaseActivity : AppCompatActivity() {

    private var progress_dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }

    fun displayProgressBar(context: Context) {
        if (progress_dialog == null) {
            progress_dialog = Dialog(context)
            progress_dialog?.setContentView(R.layout.progress_bar)
            progress_dialog?.setCancelable(false)
            progress_dialog?.show()
        }
    }

    fun hideProgressBar() {
        progress_dialog?.let {
            if (it.isShowing) {
                it.dismiss()
                progress_dialog = null
            }
        }
    }

    fun convertFoodItemToFood(foodItem: FoodItem): Food {
        return Food(
            foodName = foodItem.food_name,
            quantity = foodItem.serving_qty,
            unit = foodItem.serving_unit,
            calories = foodItem.nf_calories,
            protein = foodItem.nf_protein,
            carbs = foodItem.nf_total_carbohydrate,
            fats = foodItem.nf_total_fat,
            loggedBy = FirebaseAuth().currentUID(),
            loggedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()), // Current date
            loggedTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()) // Current time
        )
    }

}
