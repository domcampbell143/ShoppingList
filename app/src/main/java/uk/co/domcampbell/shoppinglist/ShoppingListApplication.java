package uk.co.domcampbell.shoppinglist;

import android.app.Application;

import com.firebase.client.Firebase;

import uk.co.domcampbell.shoppinglist.database.DbModule;
import uk.co.domcampbell.shoppinglist.network.NetworkModule;

/**
 * Created by Dominic on 20/06/16.
 */
public class ShoppingListApplication extends Application {

    private MyComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);

        mComponent = DaggerMyComponent.builder()
                .dbModule(new DbModule(this))
                .networkModule(new NetworkModule())
                .listPresenterModule(new ListPresenterModule())
                .build();
    }

    public MyComponent getComponent(){
        return mComponent;
    }
}
