package com.soundbliss

import android.content.Context
import com.soundbliss.Model.Message
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder

class TextMessageItem(val message: Message,
                       val context: Context) :Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
    }

    override fun getLayout() = R.layout.item_text_message

}