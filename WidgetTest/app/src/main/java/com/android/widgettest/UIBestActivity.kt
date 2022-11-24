package com.android.widgettest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.widgettest.databinding.ActivityUibestBinding

class UIBestActivity : AppCompatActivity(), View.OnClickListener {
    private val msgList = ArrayList<Msg>()
    private var adapter : MsgAdapter? = null
    lateinit var binding : ActivityUibestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUibestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initMsg()
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        adapter = MsgAdapter(msgList)
        binding.recyclerView.adapter = adapter
        binding.sendButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            binding.sendButton -> {
                val content = binding.inputText.text.toString()
                if(content.isNotEmpty()){
                    val msg = Msg(content, Msg.TYPE_SENT)
                    msgList.add(msg)
                    adapter?.notifyItemInserted(msgList.size - 1)
                    binding.recyclerView.scrollToPosition(msgList.size - 1)
                    binding.inputText.setText("")
                }
            }
        }
    }
    private fun initMsg(){
        val msg1 = Msg("How are you?", Msg.TYPE_RECEIVED)
        msgList.add(msg1)
        val msg2 = Msg("Fine, thank you, and you?", Msg.TYPE_SENT)
        msgList.add(msg2)
        val msg3 = Msg("I'm fine, too!", Msg.TYPE_RECEIVED)
        msgList.add(msg3)
    }
}