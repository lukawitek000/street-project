package com.example.streetapp.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.streetapp.MainActivity
import com.example.streetapp.R
import com.example.streetapp.databinding.CreateExerciseFragmentBinding
import com.example.streetapp.models.Exercise
import com.example.streetapp.models.Link
import kotlinx.android.synthetic.main.create_exercise_fragment.*
import java.lang.Exception
import java.lang.NumberFormatException

class CreateExercise : Fragment(), LinksAdapter.OnClearClickListener {

    companion object {
        fun newInstance() = CreateExercise()
        val TAG = CreateExercise::class.java.simpleName
    }

    //private lateinit var viewModel: CreateTrainingViewModel
    private val viewModel by navGraphViewModels<CreateTrainingViewModel>(R.id.create_training_graph)
    private lateinit var binding: CreateExerciseFragmentBinding

    private lateinit var exerciseLinksRecyclerViewAdapter : LinksAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.create_exercise_fragment, container, false)
        //viewModel = activity?.run {
       //     ViewModelProvider(this)[CreateTrainingViewModel::class.java]
       // } ?: throw Exception("Invalid Activity")

        val exerciseLinksRecyclerView : RecyclerView = binding.exerciseLinksRecyclerView
        exerciseLinksRecyclerViewAdapter  = LinksAdapter(viewModel.exerciseLinks.value, this)
        exerciseLinksRecyclerView.adapter = exerciseLinksRecyclerViewAdapter
        exerciseLinksRecyclerView.layoutManager = LinearLayoutManager(activity)




        val linksObserver = Observer<ArrayList<Link>> {
            exerciseLinksRecyclerViewAdapter.notifyDataSetChanged()
            Log.i(TAG, "links live data observer")
        }
        viewModel.exerciseLinks.observe(viewLifecycleOwner, linksObserver)


        addNewLinkToExerciseButtonListener()
        createExerciseButtonListener()

        return binding.root
    }

    private fun createExerciseButtonListener() {
        binding.exerciseAddButton.setOnClickListener {
            val newExercise = buildNewExerciseObject()

            viewModel.deleteAllExerciseLinks()

            val action = CreateExerciseDirections.actionCreateExerciseToCreateTraining2(exercise = newExercise)
            findNavController().navigate(action)

        }
    }

    private fun buildNewExerciseObject() : Exercise {
        val exerciseName = binding.exerciseNameInput.text.toString()

        val exerciseTime : Int = try {
            binding.exerciseTimeInput.text.toString().toInt()
        } catch (e: NumberFormatException) {
            0
        }
        val exerciseRepetitions: Int = try {
            binding.exerciseRepetitionInput.text.toString().toInt()
        } catch (e: NumberFormatException) {
            0
        }
        val exerciseDescription = binding.exerciseDescriptionInput.text.toString()

        val exerciseLinks = viewModel.exerciseLinks.value

        val newExercise = exerciseLinks?.let { it1 ->
            Exercise(exerciseName, exerciseTime, exerciseRepetitions, exerciseDescription,
                it1
            )
        }
        Log.i(TAG, "print new exercise $newExercise")
        return newExercise!!
    }




    private fun addNewLinkToExerciseButtonListener() {
        binding.addNewLinksToExercise.setOnClickListener {
            Log.i(TAG, "add new link to exercise")
            val linkTitle = binding.exerciseLinkTitleInput.text.toString()
            val linkUrl = binding.exerciseLinkUrlInput.text.toString()
            val newLink = Link(linkTitle, linkUrl)
            viewModel.addExerciseLink(newLink)
            binding.exerciseLinkTitleInput.text?.clear()
            binding.exerciseLinkUrlInput.text?.clear()
        }
    }

    override fun onClick(link: Link) {
        viewModel.deleteExerciseLink(link)
    }

    override fun onClickLink(link: Link) {
        (activity as MainActivity).openExternalLink(link)
    }

}