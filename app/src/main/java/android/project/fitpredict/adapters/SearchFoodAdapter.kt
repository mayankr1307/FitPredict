package android.project.fitpredict.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.project.fitpredict.R
import android.project.fitpredict.activities.SearchActivity
import android.project.fitpredict.models.FoodItem
import android.widget.Button
import com.bumptech.glide.Glide

class SearchFoodAdapter(private val context: Context, private val foodList: List<FoodItem>) : RecyclerView.Adapter<SearchFoodAdapter.FoodViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food_search, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = foodList[position]
        holder.bind(food)

        holder.itemView.findViewById<Button>(R.id.btn_add_food).setOnClickListener {
            if(context is SearchActivity) {
                context.logFood(food)
            }
        }
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val foodName: TextView = itemView.findViewById(R.id.tvFoodName)
        private val foodCalories: TextView = itemView.findViewById(R.id.tvFoodCalories)
        private val foodServingQty: TextView = itemView.findViewById(R.id.tvFoodServingQty)
        private val foodServingUnit: TextView = itemView.findViewById(R.id.tvFoodServingUnit)
        private val foodImage: ImageView = itemView.findViewById(R.id.iv_food)

        @SuppressLint("SetTextI18n")
        fun bind(food: FoodItem) {
            foodName.text = food.food_name
            foodCalories.text = "Calories: ${food.nf_calories}"
            foodServingQty.text = "Quantity: ${food.serving_qty}"
            foodServingUnit.text = "Unit: ${food.serving_unit}"

            // Load image using Glide
            val imageUrl = food.photo?.thumb
            if (imageUrl != null) {
                Glide.with(itemView.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_food_placeholder) // Optional placeholder image
                    .into(foodImage)
            } else {
                foodImage.setImageResource(R.drawable.ic_food_placeholder) // Default image if URL is null
            }
        }
    }
}
