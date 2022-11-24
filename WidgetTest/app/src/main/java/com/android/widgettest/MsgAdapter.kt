package com.android.widgettest

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.widgettest.databinding.MsgLeftItemBinding
import com.android.widgettest.databinding.MsgRightLayoutBinding

class MsgAdapter(val msgList : List<Msg>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class LeftViewHolder(binding : MsgLeftItemBinding) : RecyclerView.ViewHolder(binding.root){
        val leftMsg : TextView = binding.leftMsg
    }
    inner class RightViewHolder(binding : MsgRightLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        val rightMsg : TextView = binding.rightMsg
    }

    override fun getItemViewType(position: Int): Int {
        val msg = msgList[position]
        return msg.type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = if(viewType == Msg.TYPE_RECEIVED){
        val binding = MsgLeftItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        LeftViewHolder(binding)
    }else{
        val binding = MsgRightLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        RightViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = msgList[position]
        when(holder){
            is LeftViewHolder -> holder.leftMsg.text = msg.content
            is RightViewHolder -> holder.rightMsg.text = msg.content
            else -> throw java.lang.IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int = msgList.size
}