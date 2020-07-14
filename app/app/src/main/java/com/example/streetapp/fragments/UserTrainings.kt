package com.example.streetapp.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
import java.util.EnumSet.of
import kotlin.collections.ArrayList
import kotlin.random.Random
import kotlin.random.Random.Default.nextInt

class UserTrainings : Fragment() , UserTrainingsAdapter.OnClickTrainingHandler{

    companion object {
        fun newInstance() = UserTrainings()
        val TAG = UserTrainings::class.java.simpleName
    }

    private lateinit var viewModel: UserTrainingsViewModel

    private lateinit var recyclerViewAdapter: UserTrainingsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.user_trainings_fragment, container, false)


        //viewModel.trainings = TemporaryDatabase.getAll()
        //viewModel.allTrainings = TemporaryDatabase.getAll()
        viewModel.allTrainings.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            viewModel.trainings = it
            recyclerViewAdapter.trainings = it
            recyclerViewAdapter.notifyDataSetChanged()
            Log.i(TAG, "in observer of alltrainings")
        })

        val spanCount = activity?.windowManager?.defaultDisplay?.width
        Log.i("UserTrainings", "spanCount = $spanCount")
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerViewAdapter = UserTrainingsAdapter(this.requireActivity(), viewModel.allTrainings.value!!, this)
        recyclerView.layoutManager = GridLayoutManager(this.requireContext(), 2)
        recyclerView.adapter = recyclerViewAdapter

        val button: View = view.findViewById(R.id.floatingButton)
        button.setOnClickListener{
            Toast.makeText(context, "click nice", Toast.LENGTH_SHORT).show()
            // (activity as MainActivity).replaceFragment(CreateTraining.newInstance(), CreateTraining.TAG)
            val navController = findNavController()
            navController.navigate(R.id.action_user_trainings_to_createTraining2)
        }

        val spinner: Spinner = view.findViewById(R.id.sortBySpinner)
        val dropdownItems = arrayOf("Latest", "Alphabetically")
        spinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, dropdownItems)
        spinner.setSelection(0)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Toast.makeText(context, "what was clicked ${dropdownItems[p2]} ", Toast.LENGTH_SHORT).show()

                sortTrainings(p2)




            }

        }

        val searchEditText = view.findViewById<EditText>(R.id.filterTextInput)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                Log.i(TAG, "After text changed $p0")

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                Log.i(TAG, "Before text vhanged $p0")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int){
                Log.i(TAG , "ontextChanged $p0")
                val filterList = viewModel.allTrainings.value?.filter {
                    if (it.name.contains(p0!!, true) || it.type.contains(p0!!, true)
                        || it.description.contains(p0, true)){
                        return@filter true
                    }
                    false
                }

                viewModel.trainings = ArrayList(filterList)
                recyclerViewAdapter.trainings = viewModel.trainings
                sortTrainings(spinner.selectedItemPosition)
                recyclerViewAdapter.notifyDataSetChanged()

                Log.i(TAG, "filter list $filterList view model trainings ${viewModel.trainings}, all trainings ${viewModel.allTrainings}")
            }

        })


        setHasOptionsMenu(true)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = UserTrainingsViewModelFactory(activity as AppCompatActivity)
        viewModel = ViewModelProvider(this, viewModelFactory).get(UserTrainingsViewModel::class.java)
    }

    private fun sortTrainings(position: Int) {
        if ( position == 0) {
            sortByLatest()
        } else {
            sortByName()
        }
    }

    private fun sortByName() {
        viewModel.trainings.sortWith(compareBy {it.name})
        recyclerViewAdapter.notifyDataSetChanged()
    }

    private fun sortByLatest() {
        viewModel.trainings.sortWith(compareByDescending{it.creatingDate})
        recyclerViewAdapter.notifyDataSetChanged()
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