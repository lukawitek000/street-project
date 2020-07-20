package com.example.streetapp.fragments.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.streetapp.MainActivity
import com.example.streetapp.R
import com.example.streetapp.databinding.ExercisesItemBinding
import com.example.streetapp.fragments.trainingDetails.TrainingDetails
import com.example.streetapp.models.Exercise
import com.example.streetapp.models.Link

class ExercisesAdapter(private val exercises: ArrayList<Exercise>,
                       private val onClickExerciseListener: OnClickExerciseListener,
                       private val activity: MainActivity)
    : RecyclerView.Adapter<ExercisesAdapter.ExercisesHolder>(),
    LinksAdapter.OnClearClickListener {


    interface OnClickExerciseListener {
        fun onClickExerciseLinkDelete(exercise: Exercise, link: Link)
        fun onClickDeleteExercise(exercise: Exercise)
        fun onClickEditExercise(exercise: Exercise)
    }


    inner class ExercisesHolder(val binding : ExercisesItemBinding) : RecyclerView.ViewHolder(binding.root) {

        val exerciseLinksRecyclerView: RecyclerView = binding.exerciseLinks

        fun bind(exercise: Exercise){
            binding.exerciseName.text = exercise.name
            binding.exerciseDescription.text = exercise.description
            binding.exerciseRepetition.text = exercise.numberOfRepetitions.toString()
            binding.exerciseTime.text = exercise.time.toString()

            if(onClickExerciseListener is TrainingDetails) {
                binding.editExerciseButton.visibility = View.GONE
            }


            binding.deleteExerciseButton.setOnClickListener{
                onClickExerciseListener.onClickDeleteExercise(exercise)
            }

            binding.editExerciseButton.setOnClickListener {
                onClickExerciseListener.onClickEditExercise(exercise)
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


    override fun onBindViewHolder(holder: ExercisesHolder, position: Int) {
        val currentExercise = exercises[position]
        holder.bind(currentExercise)

        val linksLayoutManager = LinearLayoutManager(holder.exerciseLinksRecyclerView.context)
        holder.exerciseLinksRecyclerView.layoutManager = linksLayoutManager
        holder.exerciseLinksRecyclerView.adapter =
            LinksAdapter(
                currentExercise.links,
                this
            )

    }

    override fun onDeleteLinkClick(link: Link) {
        val exercisesWithGivenLink = exercises.filter {
            exercise -> for (currentLink in exercise.links) {
                if(currentLink == link) {
                    return@filter true
                }
            }
            false
        }

        if (exercisesWithGivenLink.isNullOrEmpty()){
            return
        }
        val index = exercises.indexOf(exercisesWithGivenLink[0])
        onClickExerciseListener.onClickExerciseLinkDelete(exercises[index], link)
    }

    override fun onClickLink(link: Link) {
        activity.openExternalLink(link)
    }
}