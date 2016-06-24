package uk.co.domcampbell.shoppinglist.dagger;

import dagger.Module;
import dagger.Provides;
import uk.co.domcampbell.shoppinglist.HomePresenter;
import uk.co.domcampbell.shoppinglist.database.ListDatabase;
import uk.co.domcampbell.shoppinglist.network.ListService;

/**
 * Created by Dominic on 23/06/16.
 */
@Module
public class HomePresenterModule {


    @Provides
    HomePresenter providesHomePresenter(ListDatabase listDatabase, ListService listService){
        return new HomePresenter(listDatabase, listService);
    }
}