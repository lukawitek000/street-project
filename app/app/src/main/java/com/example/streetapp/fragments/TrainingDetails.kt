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
import androidx.lifecycle.ViewModelProviders
import com.example.streetapp.R
import com.example.streetapp.models.Training


/**
 * A simple [Fragment] subclass.
 * Use the [TrainingDetails.newInstance] factory method to
 * create an instance of this fragment.
 */
class TrainingDetails : Fragment() {


    private lateinit var viewModel: TrainingDetailsViewModel
    private lateinit var viewModelFactory: TrainingDetailsViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_training_details, container, false)




        var training : Training = arguments?.get("training") as Training

        viewModelFactory = TrainingDetailsViewModelFactory(training)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TrainingDetailsViewModel::class.java)


        Toast.makeText(context, "training ${training.name}", Toast.LENGTH_SHORT).show()
        val text = view.findViewById<TextView>(R.id.textView)
        text.text = training.name

        return view
    }

    companion object {
        fun newInstance() = TrainingDetails()
        val TAG = TrainingDetails::class.java.simpleName
    }


}