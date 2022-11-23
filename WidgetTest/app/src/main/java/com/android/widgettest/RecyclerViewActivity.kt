package com.android.widgettest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.widgettest.databinding.ActivityRecyclerViewBinding

class RecyclerViewActivity : AppCompatActivity() {
    lateinit var binding : ActivityRecyclerViewBinding
    private val fruitList = ArrayList<Fruit>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFruits()
        val layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        //layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.recyclerView.layoutManager = layoutManager
        val adapter = FruitRecyclerViewAdapter(fruitList)
        binding.recyclerView.adapter = adapter
    }
    private fun initFruits(){
        repeat(2){
            fruitList.apply {
                add(Fruit(getRandomLengthString("apple"), R.drawable.apple_pic))
                add(Fruit(getRandomLengthString("banana"), R.drawable.banana_pic))
                add(Fruit(getRandomLengthString("cherry"), R.drawable.cherry_pic))
                add(Fruit(getRandomLengthString("grape"), R.drawable.grape_pic))
                add(Fruit(getRandomLengthString("mango"), R.drawable.mango_pic))
                add(Fruit(getRandomLengthString("orange"), R.drawable.orange_pic))
                add(Fruit(getRandomLengthString("pear"), R.drawable.pear_pic))
                add(Fruit(getRandomLengthString("pineapple"), R.drawable.pineapple_pic))
                add(Fruit(getRandomLengthString("strawberry"), R.drawable.strawberry_pic))
                add(Fruit(getRandomLengthString("watermelon"), R.drawable.watermelon_pic))
            }
        }
    }
    private fun getRandomLengthString(str : String) : String{
        val n = (1..20).random()
        val builder = StringBuilder()
        repeat(n){
            builder.append(str)
        }
        return builder.toString()
    }
}