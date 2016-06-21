package uk.co.domcampbell.shoppinglist;

import javax.inject.Singleton;

import dagger.Component;
import uk.co.domcampbell.shoppinglist.database.DbModule;
import uk.co.domcampbell.shoppinglist.network.NetworkModule;

/**
 * Created by Dominic on 21/06/16.
 */
@Singleton
@Component(modules = {DbModule.class, NetworkModule.class, ListPresenterModule.class})
public interface MyComponent {

    void inject(ListFragment listFragment);
}
