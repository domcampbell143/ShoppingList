package uk.co.domcampbell.shoppinglist.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Dominic on 17/01/2018.
 */

public class ConnectivityBroadcastReceiver extends BroadcastReceiver {

    private NoConnectionWrapper mNoConnectionWrapper;

    public ConnectivityBroadcastReceiver(NoConnectionWrapper noConnectionWrapper){
        mNoConnectionWrapper = noConnectionWrapper;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        mNoConnectionWrapper.setConnected(activeNetworkInfo != null && activeNetworkInfo.isConnected());
    }
}
