package uk.co.domcampbell.shoppinglist;

import android.app.Application;

import com.firebase.client.Firebase;

import java.util.UUID;

import uk.co.domcampbell.shoppinglist.database.DbModule;
import uk.co.domcampbell.shoppinglist.network.NetworkModule;

/**
 * Created by Dominic on 20/06/16.
 */
public class ShoppingListApplication extends Application {

    private MyComponent mComponent;
    private ListPresenterComponent mListPresenterComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);

        mComponent = DaggerMyComponent.builder()
                .dbModule(new DbModule(this))
                .networkModule(new NetworkModule())
                .build();
    }

    public ListPresenterComponent getListPresenterComponent(UUID uuid){
        return DaggerListPresenterComponent.builder()
                .myComponent(mComponent)
                .listPresenterModule(new ListPresenterModule(uuid))
                .build();
    }

    public HomePresenterComponent getHomePresenterComponent(){
        return DaggerHomePresenterComponent.builder()
                .myComponent(mComponent)
                .homePresenterModule(new HomePresenterModule())
                .build();
    }
}
