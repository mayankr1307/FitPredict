package android.project.fitpredict.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.project.fitpredict.R
import android.project.fitpredict.models.Food

class FoodItemAdapter(context: Context, private val foodList: List<Food>) : RecyclerView.Adapter<FoodItemAdapter.FoodViewHolder>() {
    class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodName: TextView = itemView.findViewById(R.id.tv_food_name)
        val timeStamp: TextView = itemView.findViewById(R.id.tv_time_stamp)
        val calories: TextView = itemView.findViewById(R.id.tv_calories)
        val protein: TextView = itemView.findViewById(R.id.tv_protein)
        val fats: TextView = itemView.findViewById(R.id.tv_fats)
        val carbs: TextView = itemView.findViewById(R.id.tv_carbs)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return FoodViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val currentItem = foodList[position]
        holder.foodName.text = currentItem.foodName
        holder.timeStamp.text = currentItem.loggedTime
        holder.calories.text = currentItem.calories.toString()
        holder.protein.text = currentItem.protein.toString()
        holder.fats.text = currentItem.fats.toString()
        holder.carbs.text = currentItem.carbs.toString()
    }

    override fun getItemCount() = foodList.size
}
