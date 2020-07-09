package com.example.streetapp.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.streetapp.R
import com.example.streetapp.databinding.ExercisesItemBinding
import com.example.streetapp.models.Exercise
import com.example.streetapp.models.Link
import kotlinx.android.synthetic.main.exercises_item.view.*

class ExcercisesAdapter(private val exercises: ArrayList<Exercise>) : RecyclerView.Adapter<ExcercisesAdapter.ExercisesHolder>() {


    private val viewPool = RecyclerView.RecycledViewPool()




    inner class ExercisesHolder(val binding : ExercisesItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.exercise_links)


        fun bind(exercise: Exercise){
            binding.exerciseName.text = exercise.name
            binding.exerciseDescription.text = exercise.descritption
            // binding.exerciseLinks.text = exercise.links.toString()
            binding.exerciseRepetition.text = exercise.numberOfRepetitions.toString()
            binding.exerciseTime.text = exercise.time.toString()
        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExercisesHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ExercisesItemBinding>(inflater, R.layout.exercises_item, parent, false)
        return ExercisesHolder(binding)
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    override fun onBindViewHolder(holder: ExercisesHolder, position: Int) {
        val exercise = exercises[position]
        holder.bind(exercise)

        val linksLayoutManager = LinearLayoutManager(holder.recyclerView.context)
        holder.recyclerView.layoutManager = linksLayoutManager
        holder.recyclerView.adapter = LinksAdapter(exercise.links)
        //holder.recyclerView.setRecycledViewPool(viewPool)


    }
}