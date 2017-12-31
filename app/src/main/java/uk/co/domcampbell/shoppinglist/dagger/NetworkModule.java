package uk.co.domcampbell.shoppinglist.dagger;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import uk.co.domcampbell.shoppinglist.network.ConnectivityBroadcastReceiver;
import uk.co.domcampbell.shoppinglist.network.FirebaseListService;
import uk.co.domcampbell.shoppinglist.network.ListService;
import uk.co.domcampbell.shoppinglist.user.User;

/**
 * Created by Dominic on 21/06/16.
 */
@Module
public class NetworkModule {

    Context mContext;

    public NetworkModule(Context context){
        mContext = context;
    }

    @Provides
    @Singleton
    ListService providesListService(User user){
        return new FirebaseListService(mContext, user);
    }

    @Provides
    @Singleton
    ConnectivityBroadcastReceiver providesConnectivityBroadcastReceiver(ListService listService) {
        return new ConnectivityBroadcastReceiver(listService);
    }
}
