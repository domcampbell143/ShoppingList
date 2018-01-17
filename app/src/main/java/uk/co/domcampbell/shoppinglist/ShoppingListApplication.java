package uk.co.domcampbell.shoppinglist;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.firebase.client.Firebase;

import java.util.UUID;

import uk.co.domcampbell.shoppinglist.dagger.DaggerApplicationComponent;
import uk.co.domcampbell.shoppinglist.dagger.DaggerHomePresenterComponent;
import uk.co.domcampbell.shoppinglist.dagger.DaggerListPresenterComponent;
import uk.co.domcampbell.shoppinglist.dagger.HomePresenterComponent;
import uk.co.domcampbell.shoppinglist.dagger.DbModule;
import uk.co.domcampbell.shoppinglist.dagger.ListPresenterComponent;
import uk.co.domcampbell.shoppinglist.dagger.ListPresenterModule;
import uk.co.domcampbell.shoppinglist.dagger.ApplicationComponent;
import uk.co.domcampbell.shoppinglist.dagger.NetworkModule;
import uk.co.domcampbell.shoppinglist.dagger.UserModule;

/**
 * Created by Dominic on 20/06/16.
 */
public class ShoppingListApplication extends Application {

    private ApplicationComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);

        mComponent = DaggerApplicationComponent.builder()
                .userModule(new UserModule(this))
                .dbModule(new DbModule(this))
                .networkModule(new NetworkModule(this))
                .build();

        // Register the broadcast receiver to run on a background thread
        HandlerThread handlerThread = new HandlerThread("BackgroundThread");
        handlerThread.start();
        registerReceiver(mComponent.broadcastReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION), null, new Handler(handlerThread.getLooper()));
    }

    public ApplicationComponent getApplicationComponent(){return mComponent;}

    public ListPresenterComponent getListPresenterComponent(UUID uuid){
        return DaggerListPresenterComponent.builder()
                .applicationComponent(mComponent)
                .listPresenterModule(new ListPresenterModule(uuid))
                .build();
    }

    public HomePresenterComponent getHomePresenterComponent(){
        return DaggerHomePresenterComponent.builder()
                .applicationComponent(mComponent)
                .build();
    }
}
