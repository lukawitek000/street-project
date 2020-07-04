package com.example.streetapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.DataBinderMapperImpl
import androidx.lifecycle.ViewModelProviders
import com.example.streetapp.R
import com.example.streetapp.databinding.FragmentTrainingDetailsBinding
import com.example.streetapp.models.Training
import java.lang.StringBuilder


/**
 * A simple [Fragment] subclass.
 * Use the [TrainingDetails.newInstance] factory method to
 * create an instance of this fragment.
 */
class TrainingDetails : Fragment() {


    private lateinit var viewModel: TrainingDetailsViewModel
    private lateinit var viewModelFactory: TrainingDetailsViewModelFactory
    private lateinit var binding: FragmentTrainingDetailsBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
      //  val view = inflater.inflate(R.layout.fragment_training_details, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_training_details, container, false)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //(activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)

        val training : Training = arguments?.get("training") as Training

        viewModelFactory = TrainingDetailsViewModelFactory(training)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TrainingDetailsViewModel::class.java)



        Toast.makeText(context, "training ${training.name}", Toast.LENGTH_SHORT).show()
        binding.name.text = viewModel.training.name
        binding.date.text = viewModel.training.creatingDate.toString()
        binding.type.text = viewModel.training.type
        binding.description.text = viewModel.training.description
        binding.durationTime.text = viewModel.training.timeInMinutes.toString()

        val stringBuilder = StringBuilder()

        for(str in viewModel.training.links){
            stringBuilder.append(str).append("\n")
        }

        binding.links.text = stringBuilder.toString()

        return binding.root
    }

    companion object {
        fun newInstance() = TrainingDetails()
        val TAG = TrainingDetails::class.java.simpleName
    }


    override fun onDestroy() {
        super.onDestroy()
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
}