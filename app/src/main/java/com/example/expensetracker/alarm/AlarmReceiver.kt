package com.example.expensetracker.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import com.example.expensetracker.MainActivity
import com.example.expensetracker.R

/**
 * BroadcastReceiver que se ejecuta cuando la alarma se dispara.
 * Funciona incluso con la app completamente cerrada.
 */
class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        mostrarNotificacion(context)

        // Reprogramar para el día siguiente
        val hora = ReminderPreferences.obtenerHora(context)
        val minuto = ReminderPreferences.obtenerMinuto(context)
        ReminderScheduler.programarRecordatorio(context, hora, minuto)
    }

    private fun mostrarNotificacion(context: Context) {
        val notificationManager = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Crear el canal (obligatorio desde Android 8.0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                CHANNEL_ID,
                "Recordatorios",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Recordatorios diarios de gastos"
                enableVibration(true)
                //Aquí se añade el sonido

                setSound(
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                    null
                )
                // NUEVO: Patrón de vibración personalizado
                vibrationPattern = longArrayOf(0, 500, 200, 500)
            }
            notificationManager.createNotificationChannel(canal)
        }

        // Intent para abrir la app al tocar la notificación
        val openIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            openIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // Configuracion del sonido
        val notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificacion = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("¿Registraste tus gastos?")
            .setContentText("No olvides anotar lo que gastaste hoy")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setSound(notificationSound) // Sonido
            .setVibrate(longArrayOf(0, 500, 200, 500)) // Vibración
            .build()

        notificationManager.notify(NOTIFICATION_ID, notificacion)

        // Activar vibración adicional para asegurar compatibilidad
        activarVibracion(context)
    }

    // activar vibración manualmente
    private fun activarVibracion(context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Para Android 8.0 y superior
            val vibrationEffect = VibrationEffect.createWaveform(
                longArrayOf(0, 500, 200, 500), // Patrón: espera, vibra, pausa, vibra
                -1 // No repetir
            )
            vibrator.vibrate(vibrationEffect)
        } else {
            // Para versiones anteriores
            @Suppress("DEPRECATION")
            vibrator.vibrate(longArrayOf(0, 500, 200, 500), -1)
        }
    }

    companion object {
        const val CHANNEL_ID = "expense_reminder_channel"
        const val NOTIFICATION_ID = 1001
    }
}