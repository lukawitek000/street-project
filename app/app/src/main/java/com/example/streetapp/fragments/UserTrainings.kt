package com.example.streetapp.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.streetapp.R
import com.example.streetapp.models.Training
import java.util.*
import kotlin.collections.ArrayList

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
        for(i in 0..50) {
            var random = (0..255).random().toString()
            var day = (1..30).random()
            var month = (1..12).random()
            var year = (2010..2020).random()
            trainingList.add(Training("name $i", "Handstand", "random $random", Date(year, month, day)))
        }
        val spanCount = activity?.windowManager?.defaultDisplay?.width
        Log.i("UserTrainings", "spanCount = $spanCount")
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        val recyclerViewAdapter = UserTrainingsAdapter(this.requireActivity(), trainingList)
        recyclerView.layoutManager = GridLayoutManager(this.requireContext(), 2)
        recyclerView.adapter = recyclerViewAdapter




        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(UserTrainingsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}