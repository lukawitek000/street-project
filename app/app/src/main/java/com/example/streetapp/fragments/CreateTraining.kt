package com.example.streetapp.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.streetapp.R
import com.example.streetapp.TemporaryDatabase
import com.example.streetapp.databinding.CreateTrainingFragmentBinding
import com.example.streetapp.models.Exercise
import com.example.streetapp.models.Link
import com.example.streetapp.models.Training
import java.lang.NumberFormatException
import java.sql.Time
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class CreateTraining : Fragment(){

    companion object {
        fun newInstance() = CreateTraining()
        val TAG = CreateTraining::class.java.simpleName
    }

    private lateinit var viewModel: CreateTrainingViewModel
    private lateinit var binding: CreateTrainingFragmentBinding

    private lateinit var linksRecyclerViewAdapter: LinksAdapter

    private lateinit var exercisesRecyclerViewAdapter: ExcercisesAdapter

    private lateinit var exerciseLinksRecyclerViewAdapter: LinksAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.create_training_fragment, container, false)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //(activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)



        val linksRecyclerView : RecyclerView = binding.linksRecyclerView
        linksRecyclerViewAdapter = LinksAdapter(viewModel.trainingLinksCreating)
        linksRecyclerView.layoutManager = LinearLayoutManager(activity)
        linksRecyclerView.adapter = linksRecyclerViewAdapter


        val exercisesRecyclerView : RecyclerView = binding.exercisesRecyclerView
        exercisesRecyclerViewAdapter = ExcercisesAdapter(viewModel.exercisesCreating)
        exercisesRecyclerView.layoutManager = LinearLayoutManager(activity)
        exercisesRecyclerView.adapter = exercisesRecyclerViewAdapter


        val exerciseLinksRecyclerView : RecyclerView = binding.exerciseLinksRecyclerView
        exerciseLinksRecyclerViewAdapter  = LinksAdapter(viewModel.exerciseCreatingLinks)
        exerciseLinksRecyclerView.adapter = exerciseLinksRecyclerViewAdapter
        exerciseLinksRecyclerView.layoutManager = LinearLayoutManager(activity)
        
        createButtonListener()
        trainingLinksAddButtonListener()
        exercisesAddButtonListener()
        addNewLinkToExerciseButtonListener()


        return binding.root
    }

    private fun addNewLinkToExerciseButtonListener() {
        binding.addNewLinksToExercise.setOnClickListener {
            val linkTitle = binding.exerciseLinkTitleInput.text.toString()
            val linkUrl = binding.exerciseLinkUrlInput.text.toString()
            val newLink = Link(linkTitle, linkUrl)
            viewModel.addExerciseLink(newLink)
            exerciseLinksRecyclerViewAdapter.notifyDataSetChanged()
            binding.exerciseLinkTitleInput.text?.clear()
            binding.exerciseLinkUrlInput.text?.clear()
        }
    }

    private fun exercisesAddButtonListener() {
        binding.exerciseAddButton.setOnClickListener {
            val exerciseName = binding.exerciseNameInput.text.toString()
            var exerciseTime : Int
            try {
                exerciseTime = binding.exerciseTimeInput.text.toString().toInt()
            }catch (exception: NumberFormatException) {
                exerciseTime = 0
            }
            var numberOfRepetitions : Int
            try {
                numberOfRepetitions = binding.exerciseRepetitionInput.text.toString().toInt()
            }catch (exception: NumberFormatException) {
                numberOfRepetitions = 0
            }
            val exerciseDescription = binding.exerciseDescriptionInput.text.toString()

            val exerciseLinks : ArrayList<Link> = ArrayList()
            exerciseLinks.addAll(viewModel.exerciseCreatingLinks)

            val newExercise = Exercise(exerciseName, exerciseTime, numberOfRepetitions,
                exerciseDescription, exerciseLinks)

            Log.i(TAG, "newExercise = $newExercise")
            viewModel.addExercise(newExercise)
            exercisesRecyclerViewAdapter.notifyDataSetChanged()

            viewModel.clearExerciseLinks()
            exerciseLinksRecyclerViewAdapter.notifyDataSetChanged()


            Log.i(TAG, "newExercise = $newExercise")

            binding.apply {
                exerciseNameInput.text?.clear()
                exerciseTimeInput.text?.clear()
                exerciseRepetitionInput.text?.clear()
                exerciseDescriptionInput.text?.clear()
            }



        }
    }

    private fun trainingLinksAddButtonListener() {
        binding.trainingLinkAddButton.setOnClickListener {
            val trainingLinkTitle = binding.linkTitleInput.text.toString()
            val trainingLinkUrl = binding.linkUrlInput.text.toString()
            val newLink = Link(trainingLinkTitle, trainingLinkUrl)
            Log.i(TAG, "newLink = $newLink")
            viewModel.addLink(newLink)
            // linksRecyclerViewAdapter.addLink(newLink)
            linksRecyclerViewAdapter.notifyDataSetChanged()
            binding.apply {
                linkTitleInput.text?.clear()
                linkUrlInput.text?.clear()
            }
        }
    }

    private fun createButtonListener() {
        binding.createButton.setOnClickListener {
            val trainingName = binding.trainingNameInput.text.toString()
            val trainingDescription = binding.trainingDescriptionInput.text.toString()
            var trainingTime: Int
            try {
                 trainingTime = binding.trainingTimeInput.text.toString().toInt()
            } catch (exception: NumberFormatException) {
                 trainingTime = 0
            }
            val trainingType = binding.trainingTypeInput.text.toString()
            val trainingLinks: ArrayList<Link> = viewModel.trainingLinksCreating
            val exercises: ArrayList<Exercise> = viewModel.exercisesCreating

            val newTraining = Training(trainingName, trainingType, trainingTime, trainingDescription,
            Date(1900, 12, 12), trainingLinks, exercises)

            Log.i(TAG, "inputs: $newTraining")
            TemporaryDatabase.insert(newTraining)
            Toast.makeText(context, "Training created", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_createTraining2_to_user_trainings)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CreateTrainingViewModel::class.java)
       /* viewModel.addLink(Link("First link", "urlFirst.com"))
        viewModel.addLink(Link("Second Link", "urlSecond.com"))
        viewModel.addExercise(Exercise("name",
            123, 4, "some description",
            arrayListOf(Link("link1", "url.com"), Link("link2", "url2.com"))))*/
    }


    override fun onDestroy() {
        super.onDestroy()
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }



}