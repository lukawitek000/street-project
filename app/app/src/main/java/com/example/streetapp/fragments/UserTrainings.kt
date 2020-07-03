package com.example.streetapp.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.streetapp.R
import com.example.streetapp.models.Training

class UserTrainings : Fragment() {

    companion object {
        fun newInstance() = UserTrainings()
    }

    private lateinit var viewModel: UserTrainingsViewModel





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.user_trainings_fragment, container, false)


        val trainingList = ArrayList<Training>()
        trainingList.add(Training("name1"))
        trainingList.add(Training("name2"))
        trainingList.add(Training("name3"))
        trainingList.add(Training("name4"))
        trainingList.add(Training("name5"))
        trainingList.add(Training("name6"))
        trainingList.add(Training("name7"))


        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        val recyclerViewAdapter = UserTrainingsAdapter(this.requireActivity(), trainingList)
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        recyclerView.adapter = recyclerViewAdapter




        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(UserTrainingsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}