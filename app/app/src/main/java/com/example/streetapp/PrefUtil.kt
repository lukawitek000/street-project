package com.example.streetapp

import android.content.Context
import androidx.preference.PreferenceManager
import com.example.streetapp.fragments.doTraining.DoTrainingFragment

class PrefUtil {


    companion object{

        fun getTimerLength(context: Context): Int {
            //placeholder
            return 1
        }

        private const val PREVIOUS_TIMER_LENGTH_SECONDS_ID = "previous timer length"

        fun getPreviousTimerLengthSeconds(context: Context): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, 0)
        }


        fun setPreviousTimerLengthSeconds(seconds: Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds)
            editor.apply()
        }

        private const val TIMER_STATE_ID = "timer state id"

        fun getTimerState(context: Context): DoTrainingFragment.TimerState {
            val preferneces = PreferenceManager.getDefaultSharedPreferences(context)
            val ordinal = preferneces.getInt(TIMER_STATE_ID, 0)
            return DoTrainingFragment.TimerState.values()[ordinal]
        }


        fun setTimerState(state: DoTrainingFragment.TimerState, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val ordinal = state.ordinal
            editor.putInt(TIMER_STATE_ID, ordinal)
            editor.apply()
        }



        private const val SECONDS_REMAINING_ID = "seconds remaining"

        fun getSecondsRemaining(context: Context): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(SECONDS_REMAINING_ID, 0)
        }


        fun setSecondsRemaining(seconds: Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(SECONDS_REMAINING_ID, seconds)
            editor.apply()
        }

        private const val ALARM_SET_TIME_ID = "alarm set time"

        fun getAlarmSetTime(context: Context): Long{
            val preference = PreferenceManager.getDefaultSharedPreferences(context)
            return preference.getLong(ALARM_SET_TIME_ID, 0)
        }

        fun setAlarmSetTime(time: Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(ALARM_SET_TIME_ID, time)
            editor.apply()
        }


    }


}