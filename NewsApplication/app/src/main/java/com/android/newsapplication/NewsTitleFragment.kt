package com.android.newsapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.newsapplication.databinding.NewsItemBinding
import com.android.newsapplication.databinding.NewsTitleFragBinding

class NewsTitleFragment : Fragment() {
    private var _binding : NewsTitleFragBinding? = null
    val binding get() = _binding!!
    private var isTwoPane = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = NewsTitleFragBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isTwoPane = activity?.findViewById<View>(R.id.newsContentLayout) != null
        val layoutManager = LinearLayoutManager(activity)
        binding.newsTitleRecyclerView.layoutManager = layoutManager
        val adapter = NewsAdapter(getNews())
        binding.newsTitleRecyclerView.adapter = adapter
    }

    private fun getNews() : List<News>{
        val newsList = ArrayList<News>()
        for (i in 1..50){
            val news = News("This is news title $i.", getRandomLengthString("This is news content $i."))
            newsList.add(news)
        }
        return newsList
    }
    private fun getRandomLengthString(str : String) : String{
        val n = (1..20).random()
        val builder = StringBuilder()
        repeat(n){
            builder.append(str)
        }
        return builder.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class NewsAdapter(val newsList : List<News>) : RecyclerView.Adapter<NewsAdapter.ViewHolder>(){
        inner class ViewHolder(binding: NewsItemBinding) : RecyclerView.ViewHolder(binding.root){
            val newsTitle : TextView = binding.newsTitle
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val newsItemBinding = NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            val holder = ViewHolder(newsItemBinding)
            holder.itemView.setOnClickListener {
                val news = newsList[holder.bindingAdapterPosition]
                if(isTwoPane){
                    val fragment = activity?.supportFragmentManager?.findFragmentById(R.id.newsContentFrag) as NewsContentFragment
                    fragment.refresh(news.title, news.content)
                }else{
                    NewsContentActivity.actionStart(parent.context, news.title, news.content)
                }
            }
            return holder
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val news = newsList[position]
            holder.newsTitle.text = news.title
        }

        override fun getItemCount(): Int = newsList.size
    }
}