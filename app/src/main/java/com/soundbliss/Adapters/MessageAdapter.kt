package com.soundbliss.Adapters


import android.content.Context
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.google.firebase.auth.FirebaseAuth
import com.soundbliss.Model.TextMessage
import com.soundbliss.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.text_message.view.*
import kotlinx.android.synthetic.main.user_layout.view.*
import java.text.SimpleDateFormat

class MessageAdapter(val message: TextMessage, val context: Context): Item<ViewHolder>()   {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_message.text = message.text
        setTimeMessage(viewHolder)
        setMessagePosition(viewHolder)
    }

    //set the time of the message
    private fun setTimeMessage(viewHolder: ViewHolder){
        val dateFormat = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT)
        viewHolder.itemView.textView_message_time.text = dateFormat.format(message.time)
    }

//to give the right position of a message distinguishing if is sent or received
    private fun setMessagePosition(viewHolder: ViewHolder) {
        if (message.senderId == FirebaseAuth.getInstance().currentUser?.uid) {
            viewHolder.itemView.message_position.apply {
                setBackgroundResource(R.color.white_100)
                //white if we send the message
                val layoutParams =
                    FrameLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        Gravity.END)
                this.layoutParams = layoutParams
            }
        } else {
            viewHolder.itemView.message_position.apply {
                setBackgroundResource(R.color.blue_100)
                //blue if we receive the message
                val layoutParams =
                    FrameLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        Gravity.START)
                this.layoutParams = layoutParams
            }

        }
    }
    override fun getLayout(): Int {
        return R.layout.text_message
    }

}