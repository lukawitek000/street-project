package com.example.streetapp.fragments.createTraining

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.streetapp.MainActivity
import com.example.streetapp.R
import com.example.streetapp.Status
import com.example.streetapp.databinding.CreateTrainingFragmentBinding
import com.example.streetapp.fragments.adapters.ExercisesAdapter
import com.example.streetapp.fragments.adapters.LinksAdapter
import com.example.streetapp.models.Exercise
import com.example.streetapp.models.Link
import com.example.streetapp.models.Training
import kotlinx.android.synthetic.main.create_training_fragment.*
import java.lang.Exception
import java.lang.NumberFormatException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.min
import kotlin.random.Random

class CreateTraining : Fragment(), LinksAdapter.OnClearClickListener, ExercisesAdapter.OnClickExerciseListener{

    companion object {
        fun newInstance() =
            CreateTraining()
        val TAG: String = CreateTraining::class.java.simpleName

    }

    private val viewModel by navGraphViewModels<CreateTrainingViewModel>(R.id.create_training_graph
    ) {
        CreateTrainingViewModelFactory(requireContext())
    }
    private lateinit var binding: CreateTrainingFragmentBinding

    private lateinit var linksRecyclerViewAdapter: LinksAdapter

    private lateinit var exercisesRecyclerViewAdapter: ExercisesAdapter

    private lateinit var previousTraining: Training

    private lateinit var UPDATE: String
    private lateinit var  CREATE: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        CREATE = resources.getString(R.string.create)
        UPDATE = resources.getString(R.string.update)

        binding = DataBindingUtil.inflate(inflater, R.layout.create_training_fragment, container, false)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)


        binding.numberPickerHours.minValue = 0
        binding.numberPickerHours.maxValue = 5
        binding.numberPickerMinutes.minValue = 0
        binding.numberPickerMinutes.maxValue = 59

        fillInputFields()

        Log.i(TAG, "arguments: $arguments")
        if(arguments?.get("training") != null) {
            previousTraining = arguments?.get("training") as Training
            Log.i(TAG, "get training from arguments $previousTraining")
            setValuesFromArguments(previousTraining)
            binding.createButton.text = UPDATE
            (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.update_training)
        } else {
            binding.createButton.text = CREATE
            (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.create_training)
        }





        val exercisesRecyclerView : RecyclerView = binding.exercisesRecyclerView
        exercisesRecyclerViewAdapter =
            ExercisesAdapter(
                viewModel.exercisesCreating,
                this,
                activity as MainActivity
            )
        exercisesRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        exercisesRecyclerView.adapter = exercisesRecyclerViewAdapter


        val linksRecyclerView : RecyclerView = binding.linksRecyclerView
        linksRecyclerViewAdapter =
            LinksAdapter(
                viewModel.trainingLinksCreating,
                this
            )
        linksRecyclerView.layoutManager = LinearLayoutManager(activity)
        linksRecyclerView.adapter = linksRecyclerViewAdapter


        observeStatus()
        createButtonListener()
        trainingLinksAddButtonListener()
        exercisesAddButtonListener()

        blockTrainingWithoutName()
        blockLinkWithoutUrl()


        ViewCompat.setNestedScrollingEnabled(linksRecyclerView, false)
        ViewCompat.setNestedScrollingEnabled(exercisesRecyclerView, false)


        return binding.root
    }

    private fun observeStatus() {
        viewModel.status.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(it == Status.INSERTED) {
                findNavController().navigateUp()
            }else if(it == Status.UPDATED) {
                findNavController().navigate(R.id.action_createTraining2_to_user_trainings)
            }
        })

    }


    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private fun fillInputFields() {
        binding.trainingNameInput.text = viewModel.training.name.toEditable()
        binding.trainingDescriptionInput.text = viewModel.training.description.toEditable()
        val minutes = viewModel.training.timeInMinutes % 60
        val hours = viewModel.training.timeInMinutes / 60


        binding.numberPickerHours.value = hours
        binding.numberPickerMinutes.value = minutes


        binding.trainingTypeInput.text = viewModel.training.type.toEditable()
    }


    private fun blockLinkWithoutUrl() {
        binding.trainingLinkAddButton.isEnabled = false
        binding.linkUrlInput.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                binding.trainingLinkAddButton.isEnabled = !p0.isNullOrEmpty()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.trainingLinkAddButton.isEnabled = !p0.isNullOrEmpty()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
    }

    private fun blockTrainingWithoutName() {
        binding.createButton.isEnabled = binding.createButton.text != CREATE
        binding.trainingNameInput.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                binding.createButton.isEnabled = !p0.isNullOrEmpty()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.createButton.isEnabled = !p0.isNullOrEmpty()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    private fun setValuesFromArguments(training: Training?) {
        binding.apply {
            trainingNameInput.text = training?.name?.toEditable()
            trainingDescriptionInput.text = training?.description?.toEditable()

            val minutes = training?.timeInMinutes?.rem(60)
            val hours = training?.timeInMinutes?.div(60)

            Log.i(TAG, "minutes = $minutes haour = $hours")
            if (hours != null) {
                numberPickerHours.value = hours
            }
            if (minutes != null) {
                numberPickerMinutes.value = minutes
            }


            trainingTypeInput.text = training?.type?.toEditable()
            viewModel.exercisesCreating = training?.exercises!!
            viewModel.trainingLinksCreating = training.links
        }
    }


    private fun createButtonListener() {

        binding.createButton.setOnClickListener {
            val newTraining = createNewTraining()
            if(binding.createButton.text == CREATE) {
                viewModel.insertNewTraining(newTraining)
                Toast.makeText(context, "Training created", Toast.LENGTH_SHORT).show()
            } else {
                newTraining.trainingId = previousTraining.trainingId
                viewModel.updateTraining(newTraining)
                Toast.makeText(context, "Training updated", Toast.LENGTH_SHORT).show()
            }
            (activity as MainActivity).hideKeyboard()
        }
    }

    private fun createNewTraining() : Training {
        val trainingName = binding.trainingNameInput.text.toString()
        val trainingDescription = binding.trainingDescriptionInput.text.toString()
        val trainingTime = (binding.numberPickerHours.value * 60) + binding.numberPickerMinutes.value
        val trainingType = binding.trainingTypeInput.text.toString()
        val trainingLinks: ArrayList<Link> = viewModel.trainingLinksCreating
        val exercises: ArrayList<Exercise> = viewModel.exercisesCreating

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        //val year = Random.nextInt(2000, 2020)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        return Training(name = trainingName, type = trainingType,timeInMinutes =  trainingTime,
            description = trainingDescription, creatingDate = Date(year - 1900, month, day),
            links = trainingLinks,exercises =  exercises)
    }

    private fun trainingLinksAddButtonListener() {
        binding.trainingLinkAddButton.setOnClickListener {
            val trainingLinkTitle = binding.linkTitleInput.text.toString()
            val trainingLinkUrl = binding.linkUrlInput.text.toString()
            val newLink = Link(linksTrainingOwnerId = viewModel.trainingLinksCreating.size.toLong(), title = trainingLinkTitle,url =  trainingLinkUrl)
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

        viewModel.training.timeInMinutes = (binding.numberPickerHours.value * 60) + binding.numberPickerMinutes.value
        viewModel.training.type = binding.trainingTypeInput.text.toString()
    }


    override fun onDeleteLinkClick(link: Link) {
        viewModel.trainingLinksCreating.remove(link)
        Log.i(TAG, "onDeleteLinkClick in Create Training")
        linksRecyclerViewAdapter.notifyDataSetChanged()
        if(binding.createButton.text == UPDATE){
            viewModel.deleteLink(link)
        }
    }

    override fun onClickLink(link: Link) {
        (activity as MainActivity).openExternalLink(link)
    }

    override fun onClickExerciseLinkDelete(exercise: Exercise, link: Link) {
        val index = viewModel.exercisesCreating.indexOf(exercise)
        viewModel.exercisesCreating[index].links.remove(link)
        exercisesRecyclerViewAdapter.notifyDataSetChanged()
        if(binding.createButton.text == UPDATE){
            viewModel.deleteLink(link)
        }
    }

    override fun onClickDeleteExercise(exercise: Exercise) {
        viewModel.exercisesCreating.remove(exercise)
        exercisesRecyclerViewAdapter.notifyDataSetChanged()
        if(binding.createButton.text == UPDATE) {
            viewModel.deleteExercise(exercise)
        }
    }

    override fun onClickEditExercise(exercise: Exercise) {
        viewModel.exercise = exercise
        findNavController().navigate(R.id.action_createTraining2_to_createExercise)
    }


}