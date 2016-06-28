package uk.co.domcampbell.shoppinglist.dagger;

import java.util.UUID;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import uk.co.domcampbell.shoppinglist.ListPresenter;
import uk.co.domcampbell.shoppinglist.database.ListDatabase;
import uk.co.domcampbell.shoppinglist.network.ListService;

/**
 * Created by Dominic on 21/06/16.
 */
@Module
public class ListPresenterModule {

    UUID mListUuid;

    public ListPresenterModule(UUID uuid){
        mListUuid= uuid;
    }

    @Provides @ActivityScope
    ListPresenter providesListPresenter(ListDatabase listDatabase, ListService listService){
        return new ListPresenter(listDatabase, listService, mListUuid);
    }
}
