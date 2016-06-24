package uk.co.domcampbell.shoppinglist.dagger;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import uk.co.domcampbell.shoppinglist.database.ListDatabase;
import uk.co.domcampbell.shoppinglist.database.SQLiteListDatabase;

/**
 * Created by Dominic on 21/06/16.
 */
@Module
public class DbModule {

    Application mApplication;

    public DbModule(Application application){
        mApplication = application;
    }

    @Provides
    @Singleton
    ListDatabase providesDb(){
        return new SQLiteListDatabase(mApplication);
    }
}
