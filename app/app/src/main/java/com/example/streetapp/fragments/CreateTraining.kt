package com.example.streetapp.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.streetapp.R

class CreateTraining : Fragment() {

    companion object {
        fun newInstance() = CreateTraining()
        val TAG = CreateTraining::class.java.simpleName
    }

    private lateinit var viewModel: CreateTrainingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.create_training_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CreateTrainingViewModel::class.java)
        // TODO: Use the ViewModel
    }

}