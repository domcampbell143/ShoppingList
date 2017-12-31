package uk.co.domcampbell.shoppinglist.dagger;

import android.content.BroadcastReceiver;

import javax.inject.Singleton;

import dagger.Component;
import uk.co.domcampbell.shoppinglist.ListActivity;
import uk.co.domcampbell.shoppinglist.database.ListDatabase;
import uk.co.domcampbell.shoppinglist.network.ListService;

/**
 * Created by Dominic on 21/06/16.
 */

@Singleton
@Component(modules = {DbModule.class, NetworkModule.class, UserModule.class})
public interface ApplicationComponent {

    ListDatabase listDatabase();
    ListService listService();
    BroadcastReceiver broadcastReceiver();

    void inject(ListActivity listActivity);
}
