package com.example.streetapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.streetapp.fragments.doTraining.DoTrainingFragment

class TimerExpiredReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
      //  NotificationUtil.showTimerExpired(context)

        PrefUtil.setTimerState(DoTrainingFragment.TimerState.Stopped, context)
        PrefUtil.setAlarmSetTime(0, context)
    }
}
