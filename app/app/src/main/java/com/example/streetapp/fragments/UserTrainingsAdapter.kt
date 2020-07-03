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

class UserTrainingsAdapter(val context: Activity,  var trainings: ArrayList<Training>,  var onClickTrainingHandler: OnClickTrainingHandler) :
    RecyclerView.Adapter<UserTrainingsAdapter.UserTrainingsHolder>() {


    interface OnClickTrainingHandler {
        fun onClick(training: Training)
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserTrainingsHolder {
        val rootView = LayoutInflater.from(context).inflate(R.layout.user_trainings_item, parent, false)
        return UserTrainingsHolder(rootView)
    }

    inner class UserTrainingsHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var name: TextView = itemView.findViewById<TextView>(R.id.training_name)
        var type = itemView.findViewById<TextView>(R.id.training_type)
        var createDate = itemView.findViewById<TextView>(R.id.training_date)
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            onClickTrainingHandler.onClick(trainings[adapterPosition])
        }

    }

    override fun getItemCount(): Int {
        return trainings.size
    }

    override fun onBindViewHolder(holder: UserTrainingsHolder, position: Int) {
        val training = trainings[position]
        holder.name.text = training.name
        holder.type.text = training.type
        holder.createDate.text = training.creatingDate.toString()
    }
}