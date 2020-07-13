package com.example.streetapp.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.Selection
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.streetapp.MainActivity
import com.example.streetapp.R
import com.example.streetapp.TemporaryDatabase
import com.example.streetapp.databinding.CreateTrainingFragmentBinding
import com.example.streetapp.models.Exercise
import com.example.streetapp.models.Link
import com.example.streetapp.models.Training
import java.lang.Exception
import java.lang.NumberFormatException
import java.util.*
import kotlin.collections.ArrayList

class CreateTraining : Fragment(), LinksAdapter.OnClearClickListener, ExercisesAdapter.OnClearExerciseLinkListener{

    companion object {
        fun newInstance() = CreateTraining()
        val TAG = CreateTraining::class.java.simpleName
    }

    private val viewModel by navGraphViewModels<CreateTrainingViewModel>(R.id.create_training_graph)
    private lateinit var binding: CreateTrainingFragmentBinding

    private lateinit var linksRecyclerViewAdapter: LinksAdapter

    private lateinit var exercisesRecyclerViewAdapter: ExercisesAdapter

    private lateinit var exerciseLinksRecyclerViewAdapter: LinksAdapter

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private lateinit var previousTraining: Training

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.create_training_fragment, container, false)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //(activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
       /* viewModel = activity?.run {
            ViewModelProvider(this)[CreateTrainingViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
*/




        val exercisesRecyclerView : RecyclerView = binding.exercisesRecyclerView
        exercisesRecyclerViewAdapter = ExercisesAdapter(viewModel.exercisesCreating, this, activity as MainActivity)
        exercisesRecyclerView.layoutManager = LinearLayoutManager(activity)
        exercisesRecyclerView.adapter = exercisesRecyclerViewAdapter



        Log.i(TAG, "create training exercisese creating = ${viewModel.exercisesCreating}")

        if(!arguments?.isEmpty!!) {
            Log.i(TAG, "arguments: $arguments")
            if(arguments?.get("training") != null) {
                previousTraining = arguments?.get("training") as Training
                setValuesFromArguments(previousTraining)
                binding.createButton.text = "Update"
            } else {
                binding.createButton.text = "Create"
            }
            if (arguments?.get("exercise") != null) {
                val newExercise = arguments?.get("exercise") as Exercise
                viewModel.exercisesCreating.add(newExercise)
                exercisesRecyclerViewAdapter.notifyDataSetChanged()
            }
        } else {
            Log.i(TAG, "empty arguments $arguments")
        }
        Log.i(TAG, "arguments $arguments")
        Log.i(TAG, "after argumentss create training exercisese creating = ${viewModel.exercisesCreating}")


        val linksRecyclerView : RecyclerView = binding.linksRecyclerView
        linksRecyclerViewAdapter = LinksAdapter(viewModel.trainingLinksCreating, this)
        linksRecyclerView.layoutManager = LinearLayoutManager(activity)
        linksRecyclerView.adapter = linksRecyclerViewAdapter




        createButtonListener()
        trainingLinksAddButtonListener()
        exercisesAddButtonListener()

        fillInputFields()

        return binding.root
    }

    private fun fillInputFields() {
        binding.trainingNameInput.text = viewModel.training.name.toEditable()
        binding.trainingDescriptionInput.text = viewModel.training.description.toEditable()
        binding.trainingTimeInput.text = viewModel.training.timeInMinutes.toString().toEditable()
        binding.trainingTypeInput.text = viewModel.training.type.toEditable()
    }

    private fun setValuesFromArguments(training: Training?) {
        binding.apply {
            trainingNameInput.text = training?.name?.toEditable()
            trainingDescriptionInput.text = training?.description?.toEditable()
            trainingTimeInput.text = training?.timeInMinutes?.toString()?.toEditable()
            trainingTypeInput.text = training?.type?.toEditable()
            viewModel.exercisesCreating = training?.exercises!!
            viewModel.trainingLinksCreating = training.links
            //linksRecyclerViewAdapter.notifyDataSetChanged()
            //exercisesRecyclerViewAdapter.notifyDataSetChanged()
        }
    }
/*
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
    }*/

    private fun exercisesAddButtonListener() {
        binding.addNewExerciseButton.setOnClickListener {
            saveInputs()
            findNavController().navigate(R.id.action_createTraining2_to_createExercise)
        }


        /*
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



        }*/
    }

    private fun saveInputs() {
        viewModel.training.name = binding.trainingNameInput.text.toString()
        viewModel.training.description = binding.trainingDescriptionInput.text.toString()

        viewModel.training.timeInMinutes = try {
            binding.trainingTimeInput.text.toString().toInt()
        } catch (e: Exception) {
            0
        }
        viewModel.training.type = binding.trainingTypeInput.text.toString()
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
            if(binding.createButton.text.equals("Create")) {
                TemporaryDatabase.insert(newTraining)
                Toast.makeText(context, "Training created", Toast.LENGTH_SHORT).show()
            } else {
                TemporaryDatabase.updateTraining(previousTraining, newTraining)
                Toast.makeText(context, "Training updated", Toast.LENGTH_SHORT).show()
            }

            findNavController().navigate(R.id.action_createTraining2_to_user_trainings)

            (activity as MainActivity).hideKeyboard()

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // viewModel = ViewModelProvider(this).get(CreateTrainingViewModel::class.java)
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

    override fun onClick(link: Link) {
        val success = viewModel.trainingLinksCreating.remove(link)
        if (!success) {
            //viewModel.exerciseCreatingLinks.remove(link)
            viewModel.deleteExerciseLink(link)
            exerciseLinksRecyclerViewAdapter.notifyDataSetChanged()
            return
        }
        linksRecyclerViewAdapter.notifyDataSetChanged()
    }

    override fun onClickLink(link: Link) {
        (activity as MainActivity).openExternalLink(link)
    }

    override fun onClickExerciseLinkDelete(exercise: Exercise, link: Link) {
        // val index = viewModel.training.exercises.indexOf(exercise)

        //viewModel.exerciseCreatingLinks.remove(link)
        viewModel.deleteExerciseLink(link)

        /*val e = viewModel.exerciseCreatingLinks.filter {
                clink ->
                if(clink == link) {
                    return@filter true
                }
            false
        }*/

        Log.i(TAG, "ex")

        val index = viewModel.exercisesCreating.indexOf(exercise)
        viewModel.exercisesCreating[index].links.remove(link)
        exerciseLinksRecyclerViewAdapter.notifyDataSetChanged()

    }

    override fun onClickDeleteExercise(exercise: Exercise) {
        viewModel.exercisesCreating.remove(exercise)
        exercisesRecyclerViewAdapter.notifyDataSetChanged()
    }


}