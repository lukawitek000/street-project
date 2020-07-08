package com.example.streetapp.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.*

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.streetapp.MainActivity
import com.example.streetapp.R
import com.example.streetapp.TemporaryDatabase
import com.example.streetapp.models.Exercise
import com.example.streetapp.models.Link
import com.example.streetapp.models.Training
import java.sql.Time
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



        val spanCount = activity?.windowManager?.defaultDisplay?.width
        Log.i("UserTrainings", "spanCount = $spanCount")
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        val recyclerViewAdapter = UserTrainingsAdapter(this.requireActivity(), TemporaryDatabase.getAll(), this)
        recyclerView.layoutManager = GridLayoutManager(this.requireContext(), 2)
        recyclerView.adapter = recyclerViewAdapter

        val button: View = view.findViewById(R.id.floatingButton)
        button.setOnClickListener{
            Toast.makeText(context, "click nice", Toast.LENGTH_SHORT).show()
            // (activity as MainActivity).replaceFragment(CreateTraining.newInstance(), CreateTraining.TAG)
            val navController = findNavController()
            navController.navigate(R.id.action_user_trainings_to_createTraining2)
        }

        setHasOptionsMenu(true)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(UserTrainingsViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onClick(training: Training) {
        Toast.makeText(activity, "click on ${training.name}", Toast.LENGTH_SHORT).show()
        findNavController().navigate(UserTrainingsDirections.actionUserTrainingsToTrainingDetails(training))
        /*
        val frag = TrainingDetails.newInstance()
        val bundle = Bundle()
        bundle.putSerializable("training", training)
        frag.arguments = bundle
        (activity as MainActivity).replaceFragment(frag, TrainingDetails.TAG)*/
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_bar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.settings -> {
                Toast.makeText(activity, "settings", Toast.LENGTH_SHORT).show()
                val navController = findNavController()
                navController.navigate(R.id.action_user_trainings_to_settingsFragment)
                true
            }
            else -> {
                Toast.makeText(activity, "nothing", Toast.LENGTH_SHORT).show()
                false
            }
        }

    }

}