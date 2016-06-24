package uk.co.domcampbell.shoppinglist.dagger;

import javax.inject.Singleton;

import dagger.Component;
import uk.co.domcampbell.shoppinglist.ListActivity;
import uk.co.domcampbell.shoppinglist.dagger.DbModule;
import uk.co.domcampbell.shoppinglist.database.ListDatabase;
import uk.co.domcampbell.shoppinglist.network.ListService;
import uk.co.domcampbell.shoppinglist.dagger.NetworkModule;
import uk.co.domcampbell.shoppinglist.user.User;

/**
 * Created by Dominic on 21/06/16.
 */

@Singleton
@Component(modules = {DbModule.class, NetworkModule.class, UserModule.class})
public interface ApplicationComponent {

    ListDatabase listDatabase();
    ListService listService();

    void inject(ListActivity listActivity);
}
