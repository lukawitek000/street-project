package com.example.streetapp.fragments.userTrainings

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.streetapp.R
import com.example.streetapp.databinding.UserTrainingsFragmentBinding
import com.example.streetapp.fragments.adapters.TrainingsAdapter
import com.example.streetapp.models.Training

class UserTrainings : Fragment(), TrainingsAdapter.OnClickTrainingHandler {

    companion object {
        fun newInstance() = UserTrainings()
        val TAG = UserTrainings::class.java.simpleName
    }

    private lateinit var viewModel: UserTrainingsViewModel

    private lateinit var binding: UserTrainingsFragmentBinding

    private lateinit var recyclerViewAdapter: TrainingsAdapter

    private var searchEditText: EditText? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.user_trainings_fragment, container, false)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(false)

        val viewModelFactory = UserTrainingsViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, viewModelFactory).get(UserTrainingsViewModel::class.java)
        Log.i("test", "onCreateView")

        viewModel.getAllData()

        val recyclerView = binding.recyclerView
        recyclerViewAdapter =
            TrainingsAdapter(
                this.requireActivity(),
                this,
                viewModel.trainings.value
            )
        recyclerView.layoutManager = GridLayoutManager(this.requireContext(), 2)
        recyclerView.adapter = recyclerViewAdapter


        binding.floatingButton.setOnClickListener{
            val navController = findNavController()
            navController.navigate(R.id.action_user_trainings_to_createTraining2)
        }



        binding.sortBySpinner.adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item, arrayOf("Latest", "Alphabetically"))

        binding.sortBySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Log.i(TAG, "onItemSelected $p2")
                if(viewModel.trainings.value != null) {
                    viewModel.sortTrainings(p2)
                }
            }

        }

        searchEditText = binding.filterTextInput
        searchEditText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int){
                Log.i(TAG , "on text Changed")
                viewModel.filterTrainings(p0!!, binding.sortBySpinner.selectedItemPosition)
            }

        })

        viewModel.trainings.observe(viewLifecycleOwner, Observer {
            recyclerViewAdapter.trainings = it
            recyclerViewAdapter.notifyDataSetChanged()
            Log.i(TAG, "trainings observer")
        })


        viewModel.status.observe(viewLifecycleOwner, Observer {
            if(it == UserTrainingsViewModel.Status.SUCCESS) {
                viewModel.filterTrainings(binding.filterTextInput.text.toString(), binding.sortBySpinner.selectedItemPosition)
            } else if (it == UserTrainingsViewModel.Status.FAILURE) {
                Toast.makeText(context, "Fail to connect to database", Toast.LENGTH_SHORT).show()
            }
        })



        setHasOptionsMenu(true)
        return binding.root
    }


    override fun onTrainingClick(training: Training) {
        findNavController().navigate(
            UserTrainingsDirections.actionUserTrainingsToTrainingDetails(training)
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_bar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.settings -> {
                val navController = findNavController()
                navController.navigate(R.id.action_user_trainings_to_settingsFragment)
                true
            }
            else -> {
                false
            }
        }

    }

}