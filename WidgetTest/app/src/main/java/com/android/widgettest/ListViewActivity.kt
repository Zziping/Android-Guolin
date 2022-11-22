package com.android.widgettest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.android.widgettest.databinding.ActivityListViewBinding

class ListViewActivity : AppCompatActivity() {
    private val fruitList = ArrayList<Fruit>()
    lateinit var binding : ActivityListViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFruits()
        val adapter = FruitAdapter(this, R.layout.fruit_item, fruitList)
        binding.listView.adapter = adapter
    }
    private fun initFruits(){
        repeat(2){
            fruitList.apply {
                add(Fruit("apple", R.drawable.apple_pic))
                add(Fruit("banana", R.drawable.banana_pic))
                add(Fruit("cherry", R.drawable.cherry_pic))
                add(Fruit("grape", R.drawable.grape_pic))
                add(Fruit("mango", R.drawable.mango_pic))
                add(Fruit("orange", R.drawable.orange_pic))
                add(Fruit("pear", R.drawable.pear_pic))
                add(Fruit("pineapple", R.drawable.pineapple_pic))
                add(Fruit("strawberry", R.drawable.strawberry_pic))
                add(Fruit("watermelon", R.drawable.watermelon_pic))
            }
        }
    }
}