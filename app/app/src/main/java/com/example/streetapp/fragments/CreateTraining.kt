package com.example.streetapp.fragments

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavGraph
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

class CreateTraining : Fragment(), LinksAdapter.OnClearClickListener, ExercisesAdapter.OnClickExerciseListener{

    companion object {
        fun newInstance() = CreateTraining()
        val TAG = CreateTraining::class.java.simpleName
    }

    private val viewModel by navGraphViewModels<CreateTrainingViewModel>(R.id.create_training_graph)
    private lateinit var binding: CreateTrainingFragmentBinding

    private lateinit var linksRecyclerViewAdapter: LinksAdapter

    private lateinit var exercisesRecyclerViewAdapter: ExercisesAdapter

    private lateinit var previousTraining: Training

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.create_training_fragment, container, false)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)








        fillInputFields()
        Log.i(TAG, "arguments: $arguments")
        if(arguments?.get("training") != null) {
            Log.i(TAG, "get training from arguments")
            previousTraining = arguments?.get("training") as Training
            setValuesFromArguments(previousTraining)
            binding.createButton.text = "Update"
        } else {
            binding.createButton.text = "Create"
        }

        val exercisesRecyclerView : RecyclerView = binding.exercisesRecyclerView
        exercisesRecyclerViewAdapter = ExercisesAdapter(viewModel.exercisesCreating, this, activity as MainActivity)
        exercisesRecyclerView.layoutManager = LinearLayoutManager(activity)
        exercisesRecyclerView.adapter = exercisesRecyclerViewAdapter





        val linksRecyclerView : RecyclerView = binding.linksRecyclerView
        linksRecyclerViewAdapter = LinksAdapter(viewModel.trainingLinksCreating, this)
        linksRecyclerView.layoutManager = LinearLayoutManager(activity)
        linksRecyclerView.adapter = linksRecyclerViewAdapter


        createButtonListener()
        trainingLinksAddButtonListener()
        exercisesAddButtonListener()



        return binding.root
    }

    private fun setValuesFromArguments(training: Training?) {
        binding.apply {
            trainingNameInput.text = training?.name?.toEditable()
            trainingDescriptionInput.text = training?.description?.toEditable()
            trainingTimeInput.text = training?.timeInMinutes?.toString()?.toEditable()
            trainingTypeInput.text = training?.type?.toEditable()
            //viewModel.exercisesCreating = training?.exercises!!
           // viewModel.trainingLinksCreating = training.links
            //exercisesRecyclerViewAdapter.notifyDataSetChanged()
        }
    }




    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private fun fillInputFields() {
        binding.trainingNameInput.text = viewModel.training.name.toEditable()
        binding.trainingDescriptionInput.text = viewModel.training.description.toEditable()
        binding.trainingTimeInput.text = viewModel.training.timeInMinutes.toString().toEditable()
        binding.trainingTypeInput.text = viewModel.training.type.toEditable()
    }

    private fun createButtonListener() {
        binding.createButton.setOnClickListener {
            val newTraining = createNewTraining()
            if(binding.createButton.text == "Create") {
                TemporaryDatabase.insert(newTraining)
                Toast.makeText(context, "Training created", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            } else {
                TemporaryDatabase.updateTraining(previousTraining, newTraining)
                Toast.makeText(context, "Training updated", Toast.LENGTH_SHORT).show()
                val action = CreateTrainingDirections.actionCreateTraining2ToTrainingDetails(newTraining)
                findNavController().navigate(action)
            }

            // Log.i(TAG, " back stack ${findNavController().popBackStack()}")



            (activity as MainActivity).hideKeyboard()

        }
    }

    private fun createNewTraining() : Training {
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

      //  val newTraining = Training(trainingName, trainingType, trainingTime, trainingDescription,
       //     Date(1900, 12, 12), trainingLinks, exercises)

        val newTraining = Training()

        Log.i(TAG, "inputs: $newTraining")
        return newTraining
    }

    private fun trainingLinksAddButtonListener() {
        binding.trainingLinkAddButton.setOnClickListener {
            val trainingLinkTitle = binding.linkTitleInput.text.toString()
            val trainingLinkUrl = binding.linkUrlInput.text.toString()
            val newLink = Link(1, 1, 1, trainingLinkTitle, trainingLinkUrl)
            Log.i(TAG, "newLink = $newLink")
            viewModel.addLink(newLink)
            linksRecyclerViewAdapter.notifyDataSetChanged()
            binding.apply {
                linkTitleInput.text?.clear()
                linkUrlInput.text?.clear()
            }
        }
    }




    private fun exercisesAddButtonListener() {
        binding.addNewExerciseButton.setOnClickListener {
            saveInputs()
            findNavController().navigate(R.id.action_createTraining2_to_createExercise)
        }
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







    override fun onDestroy() {
        super.onDestroy()
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onDeleteLinkClick(link: Link) {
        viewModel.trainingLinksCreating.remove(link)
        Log.i(TAG, "onDeleteLinkClick in Create Training")
        linksRecyclerViewAdapter.notifyDataSetChanged()
    }

    override fun onClickLink(link: Link) {
        (activity as MainActivity).openExternalLink(link)
    }

    override fun onClickExerciseLinkDelete(exercise: Exercise, link: Link) {
        /*val e = viewModel.exerciseCreatingLinks.filter {
                clink ->
                if(clink == link) {
                    return@filter true
                }
            false
        }*/

        Log.i(TAG, "onClickExerciseLinkDelete")
        val index = viewModel.exercisesCreating.indexOf(exercise)
        //viewModel.exercisesCreating[index].links.remove(link)
        exercisesRecyclerViewAdapter.notifyDataSetChanged()

    }

    override fun onClickDeleteExercise(exercise: Exercise) {
        viewModel.exercisesCreating.remove(exercise)
        exercisesRecyclerViewAdapter.notifyDataSetChanged()
    }

    override fun onClickEditExercise(exercise: Exercise) {
        viewModel.exercise = exercise
        findNavController().navigate(R.id.action_createTraining2_to_createExercise)
    }


}