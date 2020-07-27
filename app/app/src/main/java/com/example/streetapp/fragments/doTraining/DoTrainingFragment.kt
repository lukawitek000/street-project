package com.example.streetapp.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.streetapp.R
import com.example.streetapp.models.Training

class DoTrainingFragment : Fragment() {

    companion object {
        fun newInstance() = DoTrainingFragment()
        val TAG: String? = DoTrainingFragment::class.simpleName
    }

    private lateinit var viewModel: DoTrainingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val training = arguments?.get("training") as Training
        val viewModelFactory = DoTrainingViewModelFactory(training)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DoTrainingViewModel::class.java)




        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)




        (activity as AppCompatActivity).supportActionBar?.title = training.name

        Log.i(TAG, "training: ${viewModel.training}")

        return inflater.inflate(R.layout.do_training_fragment, container, false)
    }


}