package com.android.widgettest

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.widgettest.databinding.FruitScrollItemBinding

class FruitRecyclerViewAdapter(val fruitList : List<Fruit>) : RecyclerView.Adapter<FruitRecyclerViewAdapter.ViewHolder>() {
    inner class ViewHolder(binding: FruitScrollItemBinding) : RecyclerView.ViewHolder(binding.root){
        val fruitImage : ImageView = binding.fruitImage
        val fruitName : TextView = binding.fruitName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FruitScrollItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)
        viewHolder.fruitImage.setOnClickListener {
            val position = viewHolder.bindingAdapterPosition
            val fruit = fruitList[position]
            Toast.makeText(parent.context, "image ${fruit.name}", Toast.LENGTH_SHORT).show()
        }
        viewHolder.fruitName.setOnClickListener {
            val position = viewHolder.bindingAdapterPosition
            val fruit = fruitList[position]
            Toast.makeText(parent.context, "text ${fruit.name}", Toast.LENGTH_SHORT).show()
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fruit = fruitList[position]
        holder.fruitImage.setImageResource(fruit.imageId)
        holder.fruitName.text = fruit.name
    }

    override fun getItemCount(): Int = fruitList.size
}