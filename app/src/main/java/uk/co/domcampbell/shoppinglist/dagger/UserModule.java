package uk.co.domcampbell.shoppinglist.dagger;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import uk.co.domcampbell.shoppinglist.database.ListDatabase;
import uk.co.domcampbell.shoppinglist.database.SQLiteListDatabase;
import uk.co.domcampbell.shoppinglist.user.SharedPrefsUser;
import uk.co.domcampbell.shoppinglist.user.User;

/**
 * Created by Dominic on 24/06/16.
 */
@Module
public class UserModule {

    Context mContext;

    public UserModule(Context context){
        mContext = context;
    }

    @Provides
    @Singleton
    User providesUser(){
        return new SharedPrefsUser(mContext);
    }
}
