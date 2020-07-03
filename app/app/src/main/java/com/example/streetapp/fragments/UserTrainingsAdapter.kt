package com.example.streetapp.fragments

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.streetapp.R
import com.example.streetapp.models.Training
import kotlinx.android.synthetic.main.user_trainings_item.view.*

class UserTrainingsAdapter(val context: Activity, var trainings: ArrayList<Training>) :
    RecyclerView.Adapter<UserTrainingsAdapter.UserTrainingsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserTrainingsHolder {
        val rootView = LayoutInflater.from(context).inflate(R.layout.user_trainings_item, parent, false)
        return UserTrainingsHolder(rootView)
    }

    class UserTrainingsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById<TextView>(R.id.textView);

    }

    override fun getItemCount(): Int {
        return trainings.size
    }

    override fun onBindViewHolder(holder: UserTrainingsHolder, position: Int) {
        val training = trainings[position]
        holder.name.text = training.name
    }
}