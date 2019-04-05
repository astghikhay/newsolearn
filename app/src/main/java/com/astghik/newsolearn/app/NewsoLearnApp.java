package com.astghik.newsolearn.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.astghik.newsolearn.managers.BaseManager;
import com.astghik.newsolearn.managers.NetworkServiceManager;


public class NewsoLearnApp implements ManagerContext {

    private static NewsoLearnApp sInstance;
    private NetworkServiceManager mNetworkServiceManager;
    private boolean mInitialized;
    private Context mAppContext;
    private Context mBaseActivity;

    public static synchronized NewsoLearnApp getInstance(Context context) {
        if (sInstance == null) {
            Bundle metaData;
            try {
                ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                metaData = ai.metaData;
            } catch (PackageManager.NameNotFoundException e) {
                throw new RuntimeException(e);
            }

            String appClassName = metaData.getString("com.astghik.newsolearn.app.NewsoLearnApp");
            try {
                Class<?> appClass = NewsoLearnApp.class.getClassLoader().loadClass(appClassName);
                sInstance = (NewsoLearnApp) appClass.newInstance();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            sInstance.mAppContext = context.getApplicationContext();
            sInstance.mBaseActivity = context;
            try {
                sInstance.initialize();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return sInstance;
    }

    public synchronized void initialize() {
        if (mInitialized) {
            return;
        }

        // Manager initialization

        mNetworkServiceManager = new NetworkServiceManager(this, mBaseActivity);

        initManagers(mNetworkServiceManager);


        mInitialized = true;
    }

    private void initManagers(BaseManager... managers) {
        for (BaseManager manager : managers) {
            manager.getInitializer().init();
        }
    }


    public synchronized boolean isInitialized() {
        return mInitialized;
    }


    @Override
    public Context getAppContext() {
        return mAppContext;
    }

    @Override
    public NetworkServiceManager getNetworkServiceManager() {
        return mNetworkServiceManager;
    }

    @Override
    public void registerLocalReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        LocalBroadcastManager.getInstance(getAppContext()).registerReceiver(receiver, filter);
    }

    @Override
    public void unregisterLocalReceiver(BroadcastReceiver receiver) {
        LocalBroadcastManager.getInstance(getAppContext()).unregisterReceiver(receiver);
    }

    @Override
    public void sendLocalBroadcast(Intent intent) {
        LocalBroadcastManager.getInstance(getAppContext()).sendBroadcast(intent);
    }

    @Override
    public void saveState(Bundle state) {
        mNetworkServiceManager.onSaveInstanceState(state);
    }

    @Override
    public void loadState(Bundle state) {
        mNetworkServiceManager.onRestoreInstanceState(state);
    }
}