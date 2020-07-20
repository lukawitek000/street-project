package com.example.streetapp.fragments

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.streetapp.R
import com.example.streetapp.databinding.UserTrainingsItemBinding
import com.example.streetapp.models.Training
import java.text.SimpleDateFormat

class TrainingsAdapter(val context: Activity,  var onClickTrainingHandler: OnClickTrainingHandler,
        var trainings: List<Training>?) :
    RecyclerView.Adapter<TrainingsAdapter.UserTrainingsHolder>() {


    interface OnClickTrainingHandler {
        fun onTrainingClick(training: Training)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserTrainingsHolder {
        //val rootView = LayoutInflater.from(context).inflate(R.layout.user_trainings_item, parent, false)
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<UserTrainingsItemBinding>(inflater, R.layout.user_trainings_item, parent, false)
        return UserTrainingsHolder(binding)
    }

    inner class UserTrainingsHolder(val binding : UserTrainingsItemBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        fun bind(training: Training) {
            binding.trainingName.text = training.name
            binding.trainingType.text = training.type
            val pattern = "yyyy/MM/dd"
            val simpleDateFormat = SimpleDateFormat(pattern)
            val date: String = simpleDateFormat.format(training.creatingDate)
            binding.trainingDate.text = date

            val time = training.timeInMinutes.toString() + "min"
            binding.timeTextView.text = time
            /*var name: TextView = itemView.findViewById<TextView>(R.id.training_name)
            var type = itemView.findViewById<TextView>(R.id.training_type)
            var createDate = itemView.findViewById<TextView>(R.id.training_date)
            var timeInMinutes = itemView.findViewById<TextView>(R.id.timeTextView)*/
        }
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            trainings?.get(adapterPosition)?.let { onClickTrainingHandler.onTrainingClick(it) }
        }

    }

    override fun getItemCount(): Int {
        return trainings?.size ?: 0
    }

    override fun onBindViewHolder(holder: UserTrainingsHolder, position: Int) {
        val training = trainings?.get(position)
        if (training != null) {
            holder.bind(training)
        }
        /*holder.name.text = training.name
        holder.type.text = training.type
        val time = training.timeInMinutes.toString() + "min"
        holder.timeInMinutes.text = time


        val pattern = "yyyy/MM/dd"
        val simpleDateFormat = SimpleDateFormat(pattern)
        val date: String = simpleDateFormat.format(training.creatingDate)
        holder.createDate.text = date*/

    }
}