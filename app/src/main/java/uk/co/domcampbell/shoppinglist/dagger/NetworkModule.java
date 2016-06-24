package uk.co.domcampbell.shoppinglist.dagger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import uk.co.domcampbell.shoppinglist.network.FirebaseListService;
import uk.co.domcampbell.shoppinglist.network.ListService;
import uk.co.domcampbell.shoppinglist.user.User;

/**
 * Created by Dominic on 21/06/16.
 */
@Module
public class NetworkModule {

    @Provides
    @Singleton
    ListService providesListService(User user){
        return new FirebaseListService(user);
    }
}
