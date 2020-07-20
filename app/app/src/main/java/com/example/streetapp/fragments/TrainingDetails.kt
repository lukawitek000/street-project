package com.example.streetapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.streetapp.MainActivity
import com.example.streetapp.R
import com.example.streetapp.databinding.FragmentTrainingDetailsBinding
import com.example.streetapp.models.Exercise
import com.example.streetapp.models.Link
import com.example.streetapp.models.Training
import java.text.SimpleDateFormat


/**
 * A simple [Fragment] subclass.
 * Use the [TrainingDetails.newInstance] factory method to
 * create an instance of this fragment.
 */
class TrainingDetails : Fragment(), LinksAdapter.OnClearClickListener, ExercisesAdapter.OnClickExerciseListener {


    private lateinit var viewModel: TrainingDetailsViewModel
    private lateinit var viewModelFactory: TrainingDetailsViewModelFactory
    private lateinit var binding: FragmentTrainingDetailsBinding



    private lateinit var recyclerViewLinksAdapter : LinksAdapter
    private lateinit var recyclerViewAdapter: ExercisesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
      //  val view = inflater.inflate(R.layout.fragment_training_details, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_training_details, container, false)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //(activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)



        val training : Training = arguments?.get("training") as Training
        Log.i(TAG, "training from arguments $training , arguments $arguments")


        viewModelFactory = TrainingDetailsViewModelFactory(training, activity = activity as AppCompatActivity)
        viewModel = ViewModelProvider(this, viewModelFactory).get(TrainingDetailsViewModel::class.java)



        //Toast.makeText(context, "training ${training.name}", Toast.LENGTH_SHORT).show()
        binding.name.text = viewModel.training.name
        //binding.date.text = viewModel.training.creatingDate.toString()




        val pattern = "yyyy/MM/dd"
        val simpleDateFormat = SimpleDateFormat(pattern)
        val date: String = simpleDateFormat.format(viewModel.training.creatingDate)
        binding.date.text = date






        binding.type.text = viewModel.training.type
        binding.description.text = viewModel.training.description
        binding.durationTime.text = viewModel.training.timeInMinutes.toString()

        val recyclerViewLinks = binding.trainingLinks
        recyclerViewLinksAdapter = LinksAdapter(viewModel.training.links, this)
        recyclerViewLinks.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerViewLinks.adapter = recyclerViewLinksAdapter



        val recyclerView = binding.exerciseLinksRecyclerView
        recyclerViewAdapter = ExercisesAdapter(viewModel.training.exercises, this, activity as MainActivity)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = recyclerViewAdapter


        binding.deleteTrainingButton.setOnClickListener{
            Toast.makeText(activity, "Training deleted", Toast.LENGTH_SHORT).show()
            viewModel.deleteTraining()
            findNavController().navigateUp()
        }


       // binding.editTrainingButton.visibility = View.GONE

        binding.editTrainingButton.setOnClickListener {
            findNavController().navigate(TrainingDetailsDirections.actionTrainingDetailsToCreateTraining2(training))
        }



        return binding.root
    }

    companion object {
        fun newInstance() = TrainingDetails()
        val TAG = TrainingDetails::class.java.simpleName
    }


    override fun onDestroy() {
        super.onDestroy()
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onDeleteLinkClick(link: Link) {
        val success = viewModel.training.links.remove(link)
        if (!success) {
            viewModel.training.exercises
        }
        viewModel.deleteLink(link)
        recyclerViewLinksAdapter.notifyDataSetChanged()
    }

    override fun onClickLink(link: Link) {
        /*Toast.makeText(activity, "Link clicked", Toast.LENGTH_SHORT).show()
        val intent: Intent = Intent(Intent.ACTION_VIEW)

        var uriAddress = link.url
        if (!uriAddress.startsWith("http://") && !uriAddress.startsWith("https://")){
            uriAddress = "http://$uriAddress"
        }
        intent.data = Uri.parse(uriAddress)
        //intent.`package` = "com.google.android.d"
        startActivity(intent)*/
        (activity as MainActivity).openExternalLink(link)


    }

    override fun onClickExerciseLinkDelete(exercise: Exercise, link: Link) {
        val index = viewModel.training.exercises.indexOf(exercise)

        viewModel.training.exercises[index].links.remove(link)
        viewModel.deleteLink(link)
        recyclerViewAdapter.notifyDataSetChanged()

    }

    override fun onClickDeleteExercise(exercise: Exercise) {
        viewModel.training.exercises.remove(exercise)
        recyclerViewAdapter.notifyDataSetChanged()
        viewModel.deleteExercise(exercise)
    }

    override fun onClickEditExercise(exercise: Exercise) {
        // findNavController().navigate(R.id.action_trainingDetails_to_createExercise2)
    }
}