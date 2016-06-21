package uk.co.domcampbell.shoppinglist;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import uk.co.domcampbell.shoppinglist.database.ListDatabase;
import uk.co.domcampbell.shoppinglist.network.ListService;

/**
 * Created by Dominic on 21/06/16.
 */
@Module
public class ListPresenterModule {

    @Provides
    @Singleton
    ListPresenter providesListPresenter(ListDatabase listDatabase, ListService listService){
        return new ListPresenter(listDatabase, listService);
    }
}
