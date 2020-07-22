package com.example.streetapp.fragments.trainingDetails

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.streetapp.MainActivity
import com.example.streetapp.R
import com.example.streetapp.databinding.FragmentTrainingDetailsBinding
import com.example.streetapp.fragments.adapters.ExercisesAdapter
import com.example.streetapp.fragments.adapters.LinksAdapter
import com.example.streetapp.models.Exercise
import com.example.streetapp.models.Link
import com.example.streetapp.models.Training
import java.text.SimpleDateFormat


class TrainingDetails : Fragment(), LinksAdapter.OnClearClickListener,
    ExercisesAdapter.OnClickExerciseListener {

    companion object {
        fun newInstance() = TrainingDetails()
        val TAG: String = TrainingDetails::class.java.simpleName
    }

    private lateinit var viewModel: TrainingDetailsViewModel


    private lateinit var binding: FragmentTrainingDetailsBinding


    private lateinit var recyclerViewLinksAdapter : LinksAdapter
    private lateinit var recyclerViewExercisesAdapter: ExercisesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_training_details, container, false)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)


        (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.training_details)

        val training : Training = arguments?.get("training") as Training
        Log.i(TAG, "training from arguments $training , arguments $arguments")


        val viewModelFactory =
            TrainingDetailsViewModelFactory(
                training, requireContext())
        viewModel = ViewModelProvider(this, viewModelFactory).get(TrainingDetailsViewModel::class.java)

        populateUIWithData()


        val recyclerViewLinks = binding.trainingLinks
        recyclerViewLinksAdapter =
            LinksAdapter(
                viewModel.training.links,
                this
            )
        recyclerViewLinks.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerViewLinks.adapter = recyclerViewLinksAdapter



        val recyclerView = binding.exerciseLinksRecyclerView
        recyclerViewExercisesAdapter =
            ExercisesAdapter(
                viewModel.training.exercises,
                this, activity as MainActivity
            )
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = recyclerViewExercisesAdapter


        binding.deleteTrainingButton.setOnClickListener{
            Toast.makeText(activity, "Training deleted", Toast.LENGTH_SHORT).show()
            viewModel.deleteTraining()
            findNavController().navigateUp()
        }

        binding.editTrainingButton.setOnClickListener {
            findNavController().navigate(
                TrainingDetailsDirections.actionTrainingDetailsToCreateTraining2(viewModel.training)
            )
        }


        if(binding.description.text.isNullOrEmpty()){
            binding.description.visibility = View.GONE
            binding.descriptionLabel.visibility = View.GONE
        }

        if(binding.durationTime.text.isNullOrEmpty() || binding.durationTime.text == "0"){
            binding.durationTime.visibility = View.GONE
            binding.durationTimeLabel.visibility = View.GONE
            binding.durationTimeMin.visibility = View.GONE
        }

        if(binding.type.text.isNullOrEmpty()){
            binding.type.visibility = View.GONE
        }

        if(viewModel.training.exercises.isNullOrEmpty()){
            binding.exerciseLabel.visibility = View.GONE
        }





        return binding.root
    }

    private fun populateUIWithData() {
        binding.name.text = viewModel.training.name

        val pattern = "dd-MM-yyyy"
        val simpleDateFormat =  SimpleDateFormat(pattern)
        val date: String = simpleDateFormat.format(viewModel.training.creatingDate)
        binding.date.text = date

        binding.type.text = viewModel.training.type
        binding.description.text = viewModel.training.description
        binding.durationTime.text = viewModel.training.timeInMinutes.toString()
    }


    override fun onDeleteLinkClick(link: Link) {
        viewModel.training.links.remove(link)
        viewModel.deleteLink(link)
        recyclerViewLinksAdapter.notifyDataSetChanged()
    }

    override fun onClickLink(link: Link) {
        (activity as MainActivity).openExternalLink(link)
    }

    override fun onClickExerciseLinkDelete(exercise: Exercise, link: Link) {
        val index = viewModel.training.exercises.indexOf(exercise)
        viewModel.training.exercises[index].links.remove(link)
        viewModel.deleteLink(link)
        recyclerViewExercisesAdapter.notifyDataSetChanged()

    }

    override fun onClickDeleteExercise(exercise: Exercise) {
        viewModel.training.exercises.remove(exercise)
        recyclerViewExercisesAdapter.notifyDataSetChanged()
        viewModel.deleteExercise(exercise)
    }

    override fun onClickEditExercise(exercise: Exercise) {}
}