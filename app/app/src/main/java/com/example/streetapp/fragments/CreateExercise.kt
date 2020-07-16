package com.example.streetapp.fragments

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.streetapp.MainActivity
import com.example.streetapp.R
import com.example.streetapp.databinding.CreateExerciseFragmentBinding
import com.example.streetapp.models.Exercise
import com.example.streetapp.models.Link
import java.lang.NumberFormatException

class CreateExercise : Fragment(), LinksAdapter.OnClearClickListener {

    companion object {
        fun newInstance() = CreateExercise()
        val TAG = CreateExercise::class.java.simpleName
    }

    private val viewModel by navGraphViewModels<CreateTrainingViewModel>(R.id.create_training_graph
    ) { CreateTrainingViewModelFactory(activity as AppCompatActivity) }
    private lateinit var binding: CreateExerciseFragmentBinding

    private lateinit var exerciseLinksRecyclerViewAdapter : LinksAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.create_exercise_fragment, container, false)

        val exerciseLinksRecyclerView : RecyclerView = binding.exerciseLinksRecyclerView
        exerciseLinksRecyclerViewAdapter  = LinksAdapter(viewModel.exerciseLinks.value, this)
        exerciseLinksRecyclerView.adapter = exerciseLinksRecyclerViewAdapter
        exerciseLinksRecyclerView.layoutManager = LinearLayoutManager(activity)





        val linksObserver = Observer<ArrayList<Link>> {
            exerciseLinksRecyclerViewAdapter.notifyDataSetChanged()
            Log.i(TAG, "links live data observer")
        }
        viewModel.exerciseLinks.observe(viewLifecycleOwner, linksObserver)


        getDataToUpdate()

        addNewLinkToExerciseButtonListener()
        createExerciseButtonListener()

        return binding.root
    }

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private fun getDataToUpdate() {
        if (viewModel.exercise != null) {
            binding.exerciseDescriptionInput.text = viewModel.exercise!!.description.toEditable()
            binding.exerciseNameInput.text = viewModel.exercise!!.name.toEditable()

            binding.exerciseRepetitionInput.text = viewModel.exercise!!.numberOfRepetitions.toString().toEditable()
            binding.exerciseTimeInput.text = viewModel.exercise!!.time.toString().toEditable()

            // exerciseLinksRecyclerViewAdapter.links = viewModel.exercise!!.links
            // viewModel.populateExerciseLinks(viewModel.exercise!!.links)

            binding.exerciseAddButton.text = "Update"
        } else {
            binding.exerciseAddButton.text = "Create"
        }
    }


    private fun addNewLinkToExerciseButtonListener() {
        binding.addNewLinksToExercise.setOnClickListener {
            Log.i(TAG, "add new link to exercise")
            val linkTitle = binding.exerciseLinkTitleInput.text.toString()
            val linkUrl = binding.exerciseLinkUrlInput.text.toString()
            val newLink = Link(1, 1, 1, linkTitle, linkUrl)
            viewModel.addExerciseLink(newLink)
            binding.exerciseLinkTitleInput.text?.clear()
            binding.exerciseLinkUrlInput.text?.clear()
        }
    }




    private fun createExerciseButtonListener() {
        binding.exerciseAddButton.setOnClickListener {
            val newExercise = buildNewExerciseObject()
            if (binding.exerciseAddButton.text == "Create") {
                Log.i("TrainingDao", "exercise to add $newExercise")
                viewModel.addExercise(newExercise)

            } else {
                viewModel.updateExercise(newExercise)
                viewModel.exercise = null
            }


            viewModel.deleteAllExerciseLinks()
            findNavController().navigateUp()
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
            Exercise(name = exerciseName, time = exerciseTime, numberOfRepetitions = exerciseRepetitions,
                description = exerciseDescription,
                links = it1
            )
        }
       // val newExercise = Exercise()
        Log.i(TAG, "print new exercise $newExercise")
        return newExercise!!
    }


    override fun onDeleteLinkClick(link: Link) {
        viewModel.deleteExerciseLink(link)
    }

    override fun onClickLink(link: Link) {
        (activity as MainActivity).openExternalLink(link)
    }

}