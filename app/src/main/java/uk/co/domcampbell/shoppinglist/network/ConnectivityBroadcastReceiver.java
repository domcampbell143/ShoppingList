package uk.co.domcampbell.shoppinglist.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Dominic on 07-Oct-16.
 */

public class ConnectivityBroadcastReceiver extends BroadcastReceiver {

    ListService mListService;

    public ConnectivityBroadcastReceiver(ListService listService){
        mListService=listService;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        mListService.onNetworkConnectivityChange(activeNetworkInfo != null && activeNetworkInfo.isConnected());
    }
}
