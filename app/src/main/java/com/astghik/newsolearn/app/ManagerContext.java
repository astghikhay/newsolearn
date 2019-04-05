package com.astghik.newsolearn.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.astghik.newsolearn.managers.NetworkServiceManager;


public interface ManagerContext {

    Context getAppContext();


    public NetworkServiceManager getNetworkServiceManager();

    void registerLocalReceiver(BroadcastReceiver receiver, IntentFilter filter);

    void unregisterLocalReceiver(BroadcastReceiver receiver);

    void sendLocalBroadcast(Intent intent);

    void saveState(Bundle state);

    void loadState(Bundle state);

}