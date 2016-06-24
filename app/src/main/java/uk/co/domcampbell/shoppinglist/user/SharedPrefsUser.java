package uk.co.domcampbell.shoppinglist.user;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

/**
 * Created by Dominic on 24/06/16.
 */
public class SharedPrefsUser implements User {

    private static final String PREFS_NAME = "shoppinglist";
    private static final String PREFS_USER_ID = "user_id";

    Context mContext;

    public SharedPrefsUser(Context context){
        mContext = context.getApplicationContext();
    }

    @Override
    public UUID getId() {
        SharedPreferences preferences = mContext.getSharedPreferences(PREFS_NAME, 0);
        String uuidString = preferences.getString(PREFS_USER_ID, null);
        if (uuidString == null){
            UUID newUuid = UUID.randomUUID();
            preferences.edit().putString(PREFS_USER_ID, newUuid.toString()).commit();
            return newUuid;
        } else {
            return UUID.fromString(uuidString);
        }
    }
}
