package com.example.streetapp.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.streetapp.R
import com.example.streetapp.models.Exercise
import java.sql.Time

class CreateTraining : Fragment(){

    companion object {
        fun newInstance() = CreateTraining()
        val TAG = CreateTraining::class.java.simpleName
    }

    private lateinit var viewModel: CreateTrainingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.create_training_fragment, container, false)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //(activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)



        val recyclerView = view.findViewById<RecyclerView>(R.id.links_recycler_view)
        val recyclerViewAdapter = LinksAdapter(arrayListOf("First link", "Second Link", "Third link"))
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = recyclerViewAdapter


        val exercisesRecyclerView = view.findViewById<RecyclerView>(R.id.exercises_recycler_view)
        val exercisesRecyclerViewAdapter = ExcercisesAdapter(arrayListOf(Exercise("name", Time(123), 4, "some description", arrayListOf("link1", "link2"))))
        exercisesRecyclerView.layoutManager = LinearLayoutManager(activity)
        exercisesRecyclerView.adapter = exercisesRecyclerViewAdapter

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CreateTrainingViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

}