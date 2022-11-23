package com.android.widgettest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.widgettest.databinding.ActivityRecyclerViewBinding
import com.android.widgettest.databinding.FruitItemBinding

class FruitRecyclerViewAdapter(val fruitList : List<Fruit>) : RecyclerView.Adapter<FruitRecyclerViewAdapter.ViewHolder>() {
    inner class ViewHolder(binding: FruitItemBinding) : RecyclerView.ViewHolder(binding.root){
        val fruitImage : ImageView = binding.fruitImage
        val fruitName : TextView = binding.fruitName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FruitItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fruit = fruitList[position]
        holder.fruitImage.setImageResource(fruit.imageId)
        holder.fruitName.text = fruit.name
    }

    override fun getItemCount(): Int = fruitList.size
}