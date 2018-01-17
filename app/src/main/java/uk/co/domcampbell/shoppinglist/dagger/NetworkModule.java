package uk.co.domcampbell.shoppinglist.dagger;

import android.content.BroadcastReceiver;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import uk.co.domcampbell.shoppinglist.network.ConnectivityBroadcastReceiver;
import uk.co.domcampbell.shoppinglist.network.FirebaseListService;
import uk.co.domcampbell.shoppinglist.network.ListService;
import uk.co.domcampbell.shoppinglist.network.NoConnectionWrapper;
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
    NoConnectionWrapper providesNoConnectionWrapper(User user){
        return new NoConnectionWrapper(mContext, new FirebaseListService(user));
    }

    @Provides
    ListService providesListService(NoConnectionWrapper wrapper){
        return wrapper;
    }

    @Provides
    BroadcastReceiver providesBroadcastReceiver(NoConnectionWrapper wrapper) {
        return new ConnectivityBroadcastReceiver(wrapper);
    }
}
