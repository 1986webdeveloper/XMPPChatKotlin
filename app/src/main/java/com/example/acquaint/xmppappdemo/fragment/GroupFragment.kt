package com.example.acquaint.xmppappdemo.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.acquaint.xmppappdemo.R
import com.example.acquaint.xmppappdemo.adapter.GroupAdapter
import com.example.acquaint.xmppappdemo.app.XmppApp
import com.example.acquaint.xmppappdemo.model.GroupResponse
import com.example.acquaint.xmppappdemo.networking_setup.ApiClass
import com.example.acquaint.xmppappdemo.networking_setup.ApiInterface

import java.util.ArrayList

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupFragment : androidx.fragment.app.Fragment() {

    private var mGroupAdapter: GroupAdapter? = null
    private val mListOfGroups = ArrayList<String>()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contact_list, container, false)
        initialiseView(view)
        getListOfUser()
        return view
    }

    private fun getListOfUser() {
        val apiInterface = ApiClass.retrofitObject
        val call = apiInterface.getUserGroup(XmppApp.loginUserName)
        call.enqueue(object : Callback<GroupResponse> {
            override fun onResponse(call: Call<GroupResponse>, response: Response<GroupResponse>) {
                if (response.isSuccessful) {
                    val groupResponse = response.body()
                    mListOfGroups.addAll(groupResponse!!.groupname!!)
                    mGroupAdapter!!.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<GroupResponse>, t: Throwable) {

            }
        })
    }

    private fun initialiseView(view: View) {
        val recyclerView = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rv_contact_list)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        mGroupAdapter = GroupAdapter(context!!, mListOfGroups)
        recyclerView.adapter = mGroupAdapter
    }
}
