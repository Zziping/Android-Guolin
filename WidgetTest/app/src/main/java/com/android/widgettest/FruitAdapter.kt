package com.android.widgettest

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.android.widgettest.databinding.FruitItemBinding
class FruitAdapter(activity : Activity, resourceId : Int, data : List<Fruit>) : ArrayAdapter<Fruit>(activity, resourceId, data) {
    lateinit var binding : FruitItemBinding

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view : View
        val viewHolder : ViewHolder
        if(convertView == null){
            binding = FruitItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            view = binding.root
            val fruitImage : ImageView = binding.fruitImage
            val fruitName : TextView = binding.fruitName
            viewHolder = ViewHolder(fruitImage, fruitName)
            view.tag = viewHolder
        }else{
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        val fruit = getItem(position)
        if(fruit != null){
            viewHolder.fruitImage.setImageResource(fruit.imageId)
            viewHolder.fruitName.text = fruit.name
        }
        return view
    }

    inner class ViewHolder(val fruitImage : ImageView, val fruitName : TextView)
}