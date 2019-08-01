package com.example.acquaint.xmppappdemo.adapter

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.acquaint.xmppappdemo.R
import com.example.acquaint.xmppappdemo.activity.ChatActivity
import com.example.acquaint.xmppappdemo.model.User

import java.util.ArrayList

class ContactAdapter(var mContext: Context, var userList: List<User>) : androidx.recyclerview.widget.RecyclerView.Adapter<ContactAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.row_contact, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[holder.adapterPosition]
        holder.txtUserName.text = user.username
        holder.itemView.setOnClickListener {
            val intent = Intent(mContext, ChatActivity::class.java)
            intent.putExtra("EXTRA_CONTACT_JID", user.username)
            mContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        internal var txtUserName: TextView

        init {
            txtUserName = itemView.findViewById(R.id.txt_contact_name)
        }
    }
}
