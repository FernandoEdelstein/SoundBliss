package com.soundbliss.Adapters

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.soundbliss.Model.TextMessage

class MessageAdapter(var context: Context, list: List<TextMessage>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var list = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        TODO()
    }

}