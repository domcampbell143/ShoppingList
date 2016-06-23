package uk.co.domcampbell.shoppinglist;

import javax.inject.Singleton;

import dagger.Component;
import uk.co.domcampbell.shoppinglist.database.DbModule;
import uk.co.domcampbell.shoppinglist.database.ListDatabase;
import uk.co.domcampbell.shoppinglist.network.ListService;
import uk.co.domcampbell.shoppinglist.network.NetworkModule;
import uk.co.domcampbell.shoppinglist.view.ListFragment;

/**
 * Created by Dominic on 21/06/16.
 */

@Singleton
@Component(modules = {DbModule.class, NetworkModule.class})
public interface MyComponent {

    ListDatabase listDatabase();
    ListService listService();
}
