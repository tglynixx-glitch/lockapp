package com.example.lockapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Settings.canDrawOverlays(context)) {
            context.startForegroundService(Intent(context, LockService::class.java))
        }
    }
}
