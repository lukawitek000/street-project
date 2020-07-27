package com.example.streetapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.streetapp.fragments.doTraining.DoTrainingFragment

/*
class TimerNotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when(intent.action){
            AppConstants.ACTION_STOP -> {
                DoTrainingFragment.removeAlarm(context)
                PrefUtil.setTimerState(DoTrainingFragment.TimerState.Stopped, context)
                NotificationUtil.hideTimerNotification(context)
            }
            AppConstants.ACTION_PAUSE -> {
                var secondsRemaining = PrefUtil.getSecondsRemaining(context)
                val alarmSetTime = PrefUtil.getAlarmSetTime(context)
                val nowSeconds = DoTrainingFragment.nowSeconds
                secondsRemaining -= nowSeconds - alarmSetTime
                PrefUtil.setSecondsRemaining(secondsRemaining, context)

                DoTrainingFragment.removeAlarm(context)
                PrefUtil.setTimerState(DoTrainingFragment.TimerState.Paused, context)
                NotificationUtil.showTimerPaused(context)
            }
            AppConstants.ACTION_RESUME -> {
                val secondsRemaining = PrefUtil.getSecondsRemaining(context)
                val wakeUpTime = DoTrainingFragment.setAlarm(context, DoTrainingFragment.nowSeconds, secondsRemaining)
                PrefUtil.setTimerState(DoTrainingFragment.TimerState.Running, context)
                NotificationUtil.showTimerRunning(context, wakeUpTime)
            }
            AppConstants.ACTION_START -> {
                val minutesRemaining = PrefUtil.getTimerLength(context)
                val secondsRemaining = minutesRemaining * 60L
                val wakeUpTime = DoTrainingFragment.setAlarm(context, DoTrainingFragment.nowSeconds, secondsRemaining)
                PrefUtil.setTimerState(DoTrainingFragment.TimerState.Running, context)
                PrefUtil.setSecondsRemaining(secondsRemaining, context)
                NotificationUtil.showTimerRunning(context, wakeUpTime)
            }

        }
    }
}
*/