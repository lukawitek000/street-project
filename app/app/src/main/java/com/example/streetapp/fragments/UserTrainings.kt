package com.example.streetapp.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.streetapp.MainActivity
import com.example.streetapp.R
import com.example.streetapp.models.Training
import java.time.Duration
import java.time.LocalDateTime
import java.time.Month
import java.time.temporal.TemporalAdjusters.next
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random
import kotlin.random.Random.Default.nextInt

class UserTrainings : Fragment() , UserTrainingsAdapter.OnClickTrainingHandler{

    companion object {
        fun newInstance() = UserTrainings()
        val TAG = UserTrainings::class.java.simpleName
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
            var year = Random.nextInt(2010, 2020)
            var time = nextInt(10, 200)
            trainingList.add(Training("name $i", "Handstand", time, "random $random",
                Date(year -1900, month, day), arrayOf("some link $random", "next likt to some yt films $random", "or some wbsite $random")))
        }
        val spanCount = activity?.windowManager?.defaultDisplay?.width
        Log.i("UserTrainings", "spanCount = $spanCount")
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        val recyclerViewAdapter = UserTrainingsAdapter(this.requireActivity(), trainingList, this)
        recyclerView.layoutManager = GridLayoutManager(this.requireContext(), 2)
        recyclerView.adapter = recyclerViewAdapter




        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(UserTrainingsViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onClick(training: Training) {
        Toast.makeText(activity, "click on ${training.name}", Toast.LENGTH_SHORT).show()
        val frag = TrainingDetails.newInstance()
        val bundle = Bundle()
        bundle.putSerializable("training", training)
        frag.arguments = bundle
        (activity as MainActivity).replaceFragment(frag, TrainingDetails.TAG)
    }

}