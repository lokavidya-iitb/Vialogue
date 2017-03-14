package com.comp.iitb.vialogue.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.comp.iitb.vialogue.coordinators.OnProjectSaved;
import com.comp.iitb.vialogue.coordinators.SharedRuntimeContent;

/**
 * Created by ironstein on 15/03/17.
 */

public class ClosingService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("ClosingService : onBind : called");
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        // Handle application closing
        SharedRuntimeContent.pinProject(getBaseContext());
        Toast.makeText(getBaseContext(), "Project pinned", Toast.LENGTH_SHORT).show();

        // Destroy the service
        stopSelf();
    }
}
