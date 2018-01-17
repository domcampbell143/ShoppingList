package uk.co.domcampbell.shoppinglist.network.method;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Dominic on 17/01/2018.
 */

public class SharedPrefsNetworkMethodQueue implements NetworkMethodQueue {

    private static final String PREFS_NAME = "NoConnectionWrapper";
    private static final String PREFS_NO_ENTRIES = "number_entries";
    private static final String PREFS_KEYS = "key_";

    private SharedPreferences mPreferences;

    public SharedPrefsNetworkMethodQueue(Context context){
        mPreferences = context.getSharedPreferences(PREFS_NAME, 0);
    }

    @Override
    public void queueSerializedMethod(String method) {
        int noOfEntries = mPreferences.getInt(PREFS_NO_ENTRIES, 0);

        mPreferences.edit()
                .putString(PREFS_KEYS + noOfEntries, method)
                .putInt(PREFS_NO_ENTRIES, ++noOfEntries)
                .apply();
    }

    @Override
    public Queue<String> getQueuedMethods() {
        int noOfEntries = mPreferences.getInt(PREFS_NO_ENTRIES, 0);
        Queue<String> methodQueue = new LinkedList<>();

        for (int i=0;i<noOfEntries;i++){
            methodQueue.add(mPreferences.getString(PREFS_KEYS + i, ""));
        }

        mPreferences.edit().clear().apply();
        return methodQueue;
    }
}
