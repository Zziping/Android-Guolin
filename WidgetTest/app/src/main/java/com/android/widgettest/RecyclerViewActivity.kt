package com.android.widgettest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.widgettest.databinding.ActivityRecyclerViewBinding

class RecyclerViewActivity : AppCompatActivity() {
    lateinit var binding : ActivityRecyclerViewBinding
    private val fruitList = ArrayList<Fruit>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFruits()
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        val adapter = FruitRecyclerViewAdapter(fruitList)
        binding.recyclerView.adapter = adapter
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