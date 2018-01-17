package uk.co.domcampbell.shoppinglist.network.method;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Dominic on 17/01/2018.
 */

public class SharedPrefsNetworkMethodQueue implements NetworkMethodQueue {

    private static final String TAG="NetworkMethodQueue";
    private static final String PREFS_NAME = "NoConnectionWrapper";
    private static final String PREFS_NO_ENTRIES = "number_entries";
    private static final String PREFS_KEYS = "key_";


    private ObjectMapper mObjectMapper;
    private SharedPreferences mPreferences;

    public SharedPrefsNetworkMethodQueue(Context context){
        mObjectMapper = new ObjectMapper();
        mPreferences = context.getSharedPreferences(PREFS_NAME, 0);
    }

    @Override
    public void queueNetworkMethod(NetworkMethod networkMethod) {
        try {
            String method = mObjectMapper.writeValueAsString(networkMethod);
            Log.d(TAG, "Queueing " + method);
            int noOfEntries = mPreferences.getInt(PREFS_NO_ENTRIES, 0);

            mPreferences.edit()
                    .putString(PREFS_KEYS + noOfEntries, method)
                    .putInt(PREFS_NO_ENTRIES, ++noOfEntries)
                    .apply();
        }
        catch (JsonProcessingException e) {
                Log.e(TAG, "Failed to write object to json", e);
            }
    }

    @Override
    public Queue<NetworkMethod> getQueuedMethods() {
        int noOfEntries = mPreferences.getInt(PREFS_NO_ENTRIES, 0);
        Queue<NetworkMethod> methodQueue = new LinkedList<>();

        for (int i=0;i<noOfEntries;i++){
            String jsonMethod = mPreferences.getString(PREFS_KEYS + i, "");
            try {
                methodQueue.add(mObjectMapper.readValue(jsonMethod, NetworkMethod.class));
            } catch (IOException e) {
                Log.e(TAG, "Failed to convert the following json into NetworkMethod: " + jsonMethod, e);
            }
        }

        mPreferences.edit().clear().apply();
        return methodQueue;
    }
}
