package com.example.streetapp.fragments

import android.util.Log
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

class ExercisesAdapter(private val exercises: ArrayList<Exercise>,
                       private val onClearExerciseLinkListener: ExercisesAdapter.OnClearExerciseLinkListener)
    : RecyclerView.Adapter<ExercisesAdapter.ExercisesHolder>(), LinksAdapter.OnClearClickListener{


    // private val viewPool = RecyclerView.RecycledViewPool()

    interface OnClearExerciseLinkListener {
        fun onClickExerciseLinkDelete(exercise: Exercise, link: Link)
        fun onClickDeleteExercise(exercise: Exercise)
    }


    inner class ExercisesHolder(val binding : ExercisesItemBinding) : RecyclerView.ViewHolder(binding.root) {

        val recyclerView: RecyclerView = binding.root.findViewById<RecyclerView>(R.id.exercise_links)


        fun bind(exercise: Exercise){
            binding.exerciseName.text = exercise.name
            binding.exerciseDescription.text = exercise.descritption
            // binding.exerciseLinks.text = exercise.links.toString()
            binding.exerciseRepetition.text = exercise.numberOfRepetitions.toString()
            binding.exerciseTime.text = exercise.time.toString()
            Log.i("ExercisesAdapter", "bind $exercise")

            binding.deleteExerciseButton.setOnClickListener{
                onClearExerciseLinkListener.onClickDeleteExercise(exercise)
            }

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

   // private lateinit var currentExercise : Exercise

     //private lateinit var holder: ExercisesHolder

    override fun onBindViewHolder(holder: ExercisesHolder, position: Int) {
        val currentExercise = exercises[position]
        //this.holder = holder
        Log.i("ExercisesAdapter", "onBindViewHolder")
        holder.bind(currentExercise)

        val linksLayoutManager = LinearLayoutManager(holder.recyclerView.context)
        holder.recyclerView.layoutManager = linksLayoutManager
        holder.recyclerView.adapter = LinksAdapter(currentExercise.links, this)
        // holder.recyclerView.setRecycledViewPool(viewPool)


    }

    override fun onClick(link: Link) {
        Log.i("ExercisesAdapter", "onclick $link current Exercise")

        val e = exercises.filter {
            ex -> for (clink in ex.links) {
                if(clink == link) {
                    return@filter true
                }
            }
            false
        }


        val index = exercises.indexOf(e[0])


        onClearExerciseLinkListener.onClickExerciseLinkDelete(exercises[index], link)
        // this.holder.adapterPosition
        // holder.recyclerView.adapter?.notifyDataSetChanged()

    }
}