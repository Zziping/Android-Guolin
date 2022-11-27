package com.android.newsapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.newsapplication.databinding.ActivityNewsContentBinding

class NewsContentActivity : AppCompatActivity() {
    companion object{
        fun actionStart(context : Context, title : String, content : String){
            val intent = Intent(context, NewsContentActivity::class.java).apply {
                putExtra("news_title", title)
                putExtra("news_content", content)
            }
            context.startActivity(intent)
        }
    }
    lateinit var binding : ActivityNewsContentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsContentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val title = intent.getStringExtra("news_title")
        val content = intent.getStringExtra("news_content")
        if(title != null && content != null){
            val fragment = supportFragmentManager.findFragmentById(R.id.newsContentFrag) as NewsContentFragment
            fragment.refresh(title, content)
        }
    }
}