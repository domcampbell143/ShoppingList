package uk.co.domcampbell.shoppinglist.network;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Dominic on 21/06/16.
 */
@Module
public class NetworkModule {

    @Provides
    @Singleton
    ListService providesListService(){
        return new FirebaseListService();
    }
}
