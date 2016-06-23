package uk.co.domcampbell.shoppinglist;

import java.util.UUID;

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

    UUID mUUID;

    public ListPresenterModule(UUID uuid){
        mUUID= uuid;
    }

    @Provides
    ListPresenter providesListPresenter(ListDatabase listDatabase, ListService listService){
        return new ListPresenter(listDatabase, listService, mUUID);
    }
}
