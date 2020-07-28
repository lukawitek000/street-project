package com.example.streetapp.fragments.doTraining

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.streetapp.*
import com.example.streetapp.databinding.DoTrainingFragmentBinding
import com.example.streetapp.fragments.adapters.LinksAdapter
import com.example.streetapp.models.Link
import com.example.streetapp.models.Training
import kotlinx.android.synthetic.main.do_training_fragment.*
import java.util.*
import java.util.function.Predicate

class DoTrainingFragment : Fragment(), LinksAdapter.OnClearClickListener {

    companion object {
        fun newInstance() =
            DoTrainingFragment()
        val TAG: String? = DoTrainingFragment::class.simpleName



        fun setAlarm(context: Context, nowSeconds: Long, secondsRemaining: Long): Long{
            val wakeUpTime = (nowSeconds + secondsRemaining) * 1000
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeUpTime, pendingIntent)
            PrefUtil.setAlarmSetTime(nowSeconds, context)
            return wakeUpTime
        }

        fun removeAlarm(context: Context){
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
            PrefUtil.setAlarmSetTime(0, context)
        }


        val nowSeconds: Long
            get() = Calendar.getInstance().timeInMillis / 1000

    }

    private lateinit var viewModel: DoTrainingViewModel

    private lateinit var binding: DoTrainingFragmentBinding

    private lateinit var linksAdapter: LinksAdapter


    enum class TimerState{
        Stopped, Paused, Running
    }


    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds = 0L
    private var timerState = TimerState.Stopped

    private var secondsRemaining = 0L







    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.do_training_fragment, container, false)

        val training = arguments?.get("training") as Training
        val viewModelFactory =
            DoTrainingViewModelFactory(
                training
            )
        viewModel = ViewModelProvider(this, viewModelFactory).get(DoTrainingViewModel::class.java)




        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)


        (activity as AppCompatActivity).supportActionBar?.title = training.name

        Log.i(TAG, "training: ${viewModel.training}")

        val recyclerView = binding.exerciseLinksRecyclerView
        linksAdapter = LinksAdapter(viewModel.currentExercise.links, this)
        recyclerView.adapter = linksAdapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        populateUI()



        binding.nextExerciseButton.setOnClickListener {
            if(binding.nextExerciseButton.text == "Next") {
                Toast.makeText(context, "next exercise", Toast.LENGTH_SHORT).show()
                viewModel.nextExercise()
                if (viewModel.isLastExercise()) {
                    binding.nextExerciseButton.text = "Finish"
                }
                populateUI()
                linksAdapter.links = viewModel.currentExercise.links
                linksAdapter.notifyDataSetChanged()
                changeTimerVisibility()
            }else {
                findNavController().navigateUp()
            }
        }


        timerLengthSeconds = viewModel.currentExercise.time.toLong()
        binding.startButton.setOnClickListener {
            Toast.makeText(context, "start exercise", Toast.LENGTH_SHORT).show()
            startTimer()
            timerState = TimerState.Running
            updateButtons()
        }

        binding.pauseButton.setOnClickListener {
            timer.cancel()
            timerState = TimerState.Paused
            Toast.makeText(context, "pause exercise", Toast.LENGTH_SHORT).show()
            updateButtons()
        }

        binding.stopButton.setOnClickListener {
            Toast.makeText(context, "stop exercise", Toast.LENGTH_SHORT).show()
            timer.cancel()
            onTimerFinished()
        }

        ViewCompat.setNestedScrollingEnabled(recyclerView, false)

        changeTimerVisibility()

        return binding.root
    }

    fun changeTimerVisibility(){
        if (viewModel.currentExercise.time == 0){
            binding.timerLayout.visibility = View.GONE
        }else{
            binding.timerLayout.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        initTimer()

        removeAlarm(requireContext())
       // NotificationUtil.hideTimerNotification(requireContext())

    }

    override fun onPause() {
        super.onPause()
        if(timerState == TimerState.Running){
            timer.cancel()
            val wakeUpTime = setAlarm(requireContext(), nowSeconds, secondsRemaining)
           // NotificationUtil.showTimerRunning(requireContext(), wakeUpTime)
        }else if(timerState == TimerState.Paused){
            //NotificationUtil.showTimerPaused(requireContext())
        }

        PrefUtil.setPreviousTimerLengthSeconds(timerLengthSeconds, requireContext())

        PrefUtil.setSecondsRemaining(secondsRemaining, requireContext())
        PrefUtil.setTimerState(timerState, requireContext())
    }

    private fun initTimer(){
        timerState = PrefUtil.getTimerState(requireContext())
        if(timerState == TimerState.Stopped){
            setNewTimerLength()
        }else{
            setPreviousTimerLength()
        }

        secondsRemaining = if(timerState == TimerState.Running || timerState == TimerState.Paused){
            PrefUtil.getSecondsRemaining(requireContext())
        }else{
            timerLengthSeconds
        }

        val alarmSetTime = PrefUtil.getAlarmSetTime(requireContext())
        if(alarmSetTime > 0){
            secondsRemaining -= nowSeconds - alarmSetTime
        }

        if(secondsRemaining <= 0){
            onTimerFinished()
        }
        else if(timerState == TimerState.Running){
            startTimer()
        }
        updateButtons()
        updateCountdownUI()

    }


    private fun onTimerFinished(){
        timerState = TimerState.Stopped
        setNewTimerLength()
        //progress_countdown.progress = 0
        binding.progressCountdown.progress = 0
        PrefUtil.setSecondsRemaining(timerLengthSeconds, requireContext())
        secondsRemaining = timerLengthSeconds
        updateButtons()
        updateCountdownUI()

    }

    private fun startTimer(){
        timerState = TimerState.Running
        timer = object : CountDownTimer(secondsRemaining * 1000, 1000){
            override fun onFinish() = onTimerFinished()

            override fun onTick(p0: Long) {
                secondsRemaining = p0 / 1000
                updateCountdownUI()
            }

        }.start()

    }

    private fun setNewTimerLength(){
        //val lengthInMinutes = PrefUtil.getTimerLength(requireContext())
       // timerLengthSeconds = lengthInMinutes*60L

        timerLengthSeconds = viewModel.currentExercise.time.toLong()

       // progress_countdown.max = timerLengthSeconds.toInt()
        binding.progressCountdown.max = timerLengthSeconds.toInt()
    }

    private fun setPreviousTimerLength(){
        timerLengthSeconds = PrefUtil.getPreviousTimerLengthSeconds(requireContext())
        //progress_countdown.max = timerLengthSeconds.toInt()
        binding.progressCountdown.max = timerLengthSeconds.toInt()
    }



    private fun updateCountdownUI(){
        val minutes = secondsRemaining / 60
        val secondsInMinuteUntilFinished = secondsRemaining % 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        binding.timerTextView.text = "$minutes:${
        if(secondsStr.length == 2) secondsStr
        else "0" + secondsStr}"
        //progress_countdown.progress = (timerLengthSeconds - secondsRemaining).toInt()
        binding.progressCountdown.progress = (timerLengthSeconds - secondsRemaining).toInt()
    }

    private fun updateButtons(){
        when(timerState){
            TimerState.Running ->{
                binding.startButton.isEnabled = false
                binding.pauseButton.isEnabled = true
                binding.stopButton.isEnabled = true
            }
            TimerState.Stopped -> {
                binding.startButton.isEnabled = true
                binding.pauseButton.isEnabled = false
                binding.stopButton.isEnabled = false
            }
            TimerState.Paused -> {
                binding.startButton.isEnabled = true
                binding.pauseButton.isEnabled = false
                binding.stopButton.isEnabled = true
            }



        }
    }



    private fun populateUI() {
        binding.exerciseName.text = viewModel.currentExercise.name
        binding.exerciseDescription.text = viewModel.currentExercise.description
        binding.repsNumber.text = viewModel.currentExercise.series.toString() + "x" +
                viewModel.currentExercise.numberOfRepetitions.toString() + "reps"


        if(viewModel.currentExercise.time == 0){
            binding.repsTime.visibility = View.GONE
            binding.startButton.visibility = View.GONE
        }else {
            val minutes = (viewModel.currentExercise.time / 60)
            val seconds = (viewModel.currentExercise.time % 60)
            if(minutes == 0 && seconds != 0){
                binding.repsTime.text = seconds.toString() + "s"
            }else if(minutes != 0 && seconds == 0){
                binding.repsTime.text = minutes.toString() + "min"
            }else{
                binding.repsTime.text = minutes.toString() + "min " + seconds.toString() + "s"
            }
        }
    }

    override fun onDeleteLinkClick(link: Link) {
        // here deleting won't be possible
    }

    override fun onClickLink(link: Link) {
        (activity as MainActivity).openExternalLink(link)
    }


}