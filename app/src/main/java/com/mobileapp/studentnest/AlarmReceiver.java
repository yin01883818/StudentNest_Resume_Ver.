package com.mobileapp.studentnest;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;
// class to schedule alarms at a specific time.
// When this class receives an alarm, it executes
// a specific action even when the app is off screen or terminated.
// In this case, schedule notifications.
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");
        // the end date that the user chooses in milliseconds.
        long endDateMillis = intent.getLongExtra("endDateMillis", -1);
        // specific time that the user wants to receive the notifications.
        int userHour = intent.getIntExtra("userHour", 20);
        int userMinute = intent.getIntExtra("userMinute", 30);

        // Show today's notification
        createNotification(context, title, message);


        long nextTriggerTime = getNextTriggerTime(userHour, userMinute);
        // Schedule next notification (if we haven't passed end date)
        if (nextTriggerTime <= endDateMillis) {
            scheduleNextNotification(context, title, message, nextTriggerTime, endDateMillis, userHour, userMinute);
        }
    }

    private void createNotification(Context context, String title, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "your_channel_id";
        // check if android version is greater than android 8.0, so that we don't
        // create a notification channel on older versions and cause a crash.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Scheduled Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.simplifiedicon)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
    // Schedule the next time to be a day later
    private long getNextTriggerTime(int userHour, int userMinute) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1); // Move to next day
        calendar.set(Calendar.HOUR_OF_DAY, userHour);
        calendar.set(Calendar.MINUTE, userMinute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }
    // schedule next notification using intent
    private void scheduleNextNotification(Context context, String title, String message, long triggerAtMillis,
                                          long endDateMillis, int userHour, int userMinute) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("message", message);
        intent.putExtra("endDateMillis", endDateMillis);
        intent.putExtra("userHour", userHour);
        intent.putExtra("userMinute", userMinute);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                (int) triggerAtMillis, // Use a unique request code (different each day)
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        // Alarm manager is used to schedule the action at a specific time until the end date.
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
                }
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            }
        }
    }

}

