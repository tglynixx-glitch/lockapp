package com.example.lockapp

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.*
import android.widget.*

class LockService : Service() {
    private lateinit var wm: WindowManager
    private lateinit var overlay: View
    private val PASSWORD = "1111"

    override fun onCreate() {
        super.onCreate()
        startForeground(1, buildNotification())
        wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        showOverlay()
    }

    private fun buildNotification(): Notification {
        val chId = "lock_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val ch = NotificationChannel(chId, "Lock", NotificationManager.IMPORTANCE_LOW)
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(ch)
        }
        return Notification.Builder(this, chId)
            .setContentTitle("LockApp")
            .setContentText("Служба активна")
            .setSmallIcon(android.R.drawable.ic_lock_lock)
            .build()
    }

    private fun showOverlay() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        overlay = inflater.inflate(R.layout.overlay_lock, null)

        val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        else WindowManager.LayoutParams.TYPE_PHONE

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            type,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_FULLSCREEN or
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            PixelFormat.TRANSLUCENT
        )

        wm.addView(overlay, params)

        val et = overlay.findViewById<EditText>(R.id.etPass)
        val btn = overlay.findViewById<Button>(R.id.btnUnlock)
        val tv = overlay.findViewById<TextView>(R.id.tvMsg)

        btn.setOnClickListener {
            if (et.text.toString() == PASSWORD) {
                wm.removeView(overlay)
                stopSelf()
            } else {
                tv.text = "Неверный код"
                et.setText("")
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
