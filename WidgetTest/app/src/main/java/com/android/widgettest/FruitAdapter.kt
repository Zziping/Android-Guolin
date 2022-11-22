package com.android.widgettest

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.android.widgettest.databinding.FruitItemBinding

class FruitAdapter(activity : Activity, resourceId : Int, data : List<Fruit>) : ArrayAdapter<Fruit>(activity, resourceId, data) {
    lateinit var binding : FruitItemBinding
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        binding = FruitItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val fruit = getItem(position)
        if(fruit != null){
            binding.fruitImage.setImageResource(fruit.imageId)
            binding.fruitName.text = fruit.name
        }
        return binding.root
    }
}