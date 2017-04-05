package com.benedetto.lars.lab1;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Iterator;
import java.util.Set;

public class TracerActivity extends AppCompatActivity {

    private void notify(String msg) {
        //Stolen from lecture slides
        //TODO: Understand this
        String strClass = this.getClass().getName();
        String[] strings = strClass.split("\\.");
        Notification.Builder notibuild = new Notification.Builder(this);
        notibuild.setContentTitle(msg);
        notibuild.setAutoCancel(true).setSmallIcon(R.mipmap.ic_launcher);
        notibuild.setContentText(strings[strings.length - 1]);
        Notification noti = notibuild.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify((int) System.currentTimeMillis(), noti);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            this.notify(getString(R.string.create_no_state));
        } else {
            this.notify(getString(R.string.create_with_state));
            Set<String> keys = savedInstanceState.keySet();
            Iterator iter = keys.iterator();
            while(iter.hasNext())
                this.notify("key:" + (String)iter.next());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        notify("onPause()");
    }

    @Override
    protected void onRestart() {
        super.onPause();
        notify("onRestart()");
    }

    @Override
    protected void onStart() {
        super.onPause();
        notify("onStart()");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onPause();
        notify("onSaveInstanceState()");
    }

    @Override
    protected void onStop() {
        super.onPause();
        notify("onStop()");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onPause();
        notify("onActivityResult()");
    }
}
