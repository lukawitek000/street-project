package com.example.streetapp.fragments.createTraining

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.streetapp.MainActivity
import com.example.streetapp.R
import com.example.streetapp.databinding.CreateExerciseFragmentBinding
import com.example.streetapp.fragments.adapters.LinksAdapter
import com.example.streetapp.models.Exercise
import com.example.streetapp.models.Link
import java.lang.NumberFormatException

class CreateExercise : Fragment(), LinksAdapter.OnClearClickListener {

    companion object {
        fun newInstance() = CreateExercise()
        val TAG: String = CreateExercise::class.java.simpleName

    }

    private val viewModel by navGraphViewModels<CreateTrainingViewModel>(R.id.create_training_graph
    ) {
        CreateTrainingViewModelFactory(requireContext())
    }
    private lateinit var binding: CreateExerciseFragmentBinding

    private lateinit var exerciseLinksRecyclerViewAdapter : LinksAdapter

    private lateinit var UPDATE: String
    private lateinit var  CREATE: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        CREATE = resources.getString(R.string.create)
        UPDATE = resources.getString(R.string.update)

        binding = DataBindingUtil.inflate(inflater, R.layout.create_exercise_fragment, container, false)

        if(viewModel.exercise?.name.isNullOrEmpty()) {
            (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.create_exercise)
        }else{
            (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.update_exercise)
        }

        binding.numberPickerMinutes.maxValue = 60
        binding.numberPickerSeconds.maxValue = 59




        val exerciseLinksRecyclerView : RecyclerView = binding.exerciseLinksRecyclerView

        exerciseLinksRecyclerViewAdapter  = LinksAdapter(viewModel.exerciseLinks.value, this)
        exerciseLinksRecyclerView.adapter = exerciseLinksRecyclerViewAdapter
        exerciseLinksRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        viewModel.exerciseLinks.observe(viewLifecycleOwner, Observer<ArrayList<Link>> {
            exerciseLinksRecyclerViewAdapter.notifyDataSetChanged()
        })

        getDataToUpdate()
        addNewLinkToExerciseButtonListener()
        createExerciseButtonListener()
        blockExerciseWithoutName()
        blockExerciseLinkWithoutUrl()

        ViewCompat.setNestedScrollingEnabled(exerciseLinksRecyclerView, false)

        return binding.root
    }

    private fun blockExerciseWithoutName() {
        binding.exerciseAddButton.isEnabled = binding.exerciseAddButton.text != CREATE
        binding.exerciseNameInput.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                binding.exerciseAddButton.isEnabled = !p0.isNullOrEmpty()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.exerciseAddButton.isEnabled = !p0.isNullOrEmpty()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

    }

    private fun blockExerciseLinkWithoutUrl() {
        binding.addNewLinksToExercise.isEnabled = false
        binding.exerciseLinkUrlInput.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                binding.addNewLinksToExercise.isEnabled = !p0.isNullOrEmpty()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.addNewLinksToExercise.isEnabled = !p0.isNullOrEmpty()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private fun getDataToUpdate() {
        if (viewModel.exercise != null) {
            binding.exerciseDescriptionInput.text = viewModel.exercise!!.description.toEditable()
            binding.exerciseNameInput.text = viewModel.exercise!!.name.toEditable()
            binding.exerciseRepetitionInput.text = viewModel.exercise!!.numberOfRepetitions.toString().toEditable()

            val minutes = viewModel.exercise?.time?.div(60)
            val seconds = viewModel.exercise?.time?.rem(60)
            if (seconds != null) {
                binding.numberPickerSeconds.value = seconds
            }
            if (minutes != null) {
                binding.numberPickerMinutes.value = minutes
            }

            binding.seriesInput.text = viewModel.exercise!!.series.toString().toEditable()


            exerciseLinksRecyclerViewAdapter.links = viewModel.exercise!!.links
            viewModel.populateExerciseLinks(viewModel.exercise!!.links)
            binding.exerciseAddButton.text = UPDATE
        } else {
            binding.exerciseAddButton.text = CREATE
        }
    }


    private fun addNewLinkToExerciseButtonListener() {
        binding.addNewLinksToExercise.setOnClickListener {
            val linkTitle = binding.exerciseLinkTitleInput.text.toString()
            val linkUrl = binding.exerciseLinkUrlInput.text.toString()
            val newLink = Link(viewModel.exerciseLinks.value?.size?.toLong() ?: 0,
                viewModel.exercisesCreating.size.toLong(), 0, linkTitle, linkUrl)
            viewModel.addExerciseLink(newLink)
            binding.exerciseLinkTitleInput.text?.clear()
            binding.exerciseLinkUrlInput.text?.clear()
        }
    }




    private fun createExerciseButtonListener() {
        binding.exerciseAddButton.setOnClickListener {
            val newExercise = buildNewExerciseObject()
            if (binding.exerciseAddButton.text == CREATE) {
                viewModel.addExercise(newExercise)
            } else {
                newExercise.exerciseId = viewModel.exercise?.exerciseId!!
                newExercise.parentTrainingId = viewModel.exercise!!.parentTrainingId
                viewModel.updateExercise(newExercise)
            }
            viewModel.exercise = null
            viewModel.deleteAllExerciseLinks()
            findNavController().navigateUp()
        }
    }


    private fun buildNewExerciseObject() : Exercise {
        val exerciseName = binding.exerciseNameInput.text.toString()
        val exerciseTime : Int = (binding.numberPickerMinutes.value * 60) + binding.numberPickerSeconds.value
        val exerciseRepetitions: Int = try {
            binding.exerciseRepetitionInput.text.toString().toInt()
        } catch (e: NumberFormatException) {
            1
        }
        val exerciseDescription = binding.exerciseDescriptionInput.text.toString()
        val exerciseLinks = viewModel.exerciseLinks.value
        val series = try {
            binding.seriesInput.text.toString().toInt()
        }catch (e: Exception){
            1
        }
        return Exercise(name = exerciseName, time = exerciseTime, numberOfRepetitions = exerciseRepetitions,
                description = exerciseDescription,
                links = exerciseLinks!!, series = series
            )
    }


    override fun onDeleteLinkClick(link: Link) {
        viewModel.deleteExerciseLink(link)
        if(binding.exerciseAddButton.text == UPDATE){
            viewModel.deleteLink(link)
        }
    }

    override fun onClickLink(link: Link) {
        (activity as MainActivity).openExternalLink(link)
    }

}