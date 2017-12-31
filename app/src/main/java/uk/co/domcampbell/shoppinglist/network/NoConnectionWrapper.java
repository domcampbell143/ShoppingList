package uk.co.domcampbell.shoppinglist.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;

import uk.co.domcampbell.shoppinglist.dto.ListItem;
import uk.co.domcampbell.shoppinglist.dto.ShoppingList;
import uk.co.domcampbell.shoppinglist.network.method.AddItemToListMethod;
import uk.co.domcampbell.shoppinglist.network.method.AddListMethod;
import uk.co.domcampbell.shoppinglist.network.method.AddUserToListMethod;
import uk.co.domcampbell.shoppinglist.network.method.DeleteListMethod;
import uk.co.domcampbell.shoppinglist.network.method.NetworkMethod;
import uk.co.domcampbell.shoppinglist.network.method.RemoveItemFromListMethod;
import uk.co.domcampbell.shoppinglist.network.method.UpdateItemInListMethod;
import uk.co.domcampbell.shoppinglist.network.method.UpdateListNameMethod;

/**
 * Created by Dominic on 31/12/2017.
 */

public class NoConnectionWrapper extends BroadcastReceiver implements ListService {

    private static final String TAG="NoConnectionWrapper";

    private static final String PREFS_NAME = "NoConnectionWrapper";
    private static final String PREFS_NO_ENTRIES = "number_entries";
    private static final String PREFS_KEYS = "key_";

    private Context mContext;
    private boolean isConnected;
    private ListService mWrappedListService;

    private ObjectMapper mObjectMapper;

    public NoConnectionWrapper(Context context, ListService listService){
        mContext = context;
        mWrappedListService = listService;
        mObjectMapper = new ObjectMapper();

        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            isConnected = true;
            executeNetworkMethods();
        } else {
            isConnected = false;
        }
    }

    @Override
    public void addList(ShoppingList shoppingList) {
        if (isConnected){
            mWrappedListService.addList(shoppingList);
        } else {
            queueNetworkMethod(new AddListMethod(cleanShoppingList(shoppingList)));
        }
    }

    @Override
    public void addUserToNetworkList(ShoppingList shoppingList) {
        if (isConnected){
            mWrappedListService.addUserToNetworkList(shoppingList);
        } else {
            queueNetworkMethod(new AddUserToListMethod(cleanShoppingList(shoppingList)));
        }
    }

    @Override
    public void updateListName(ShoppingList shoppingList) {
        if (isConnected){
            mWrappedListService.updateListName(shoppingList);
        } else {
            queueNetworkMethod(new UpdateListNameMethod(cleanShoppingList(shoppingList)));
        }
    }

    @Override
    public void addItemToList(ListItem item, ShoppingList shoppingList) {
        if (isConnected){
            mWrappedListService.addItemToList(item, shoppingList);
        } else {
            queueNetworkMethod(new AddItemToListMethod(item, cleanShoppingList(shoppingList)));
        }
    }

    @Override
    public void removeItemFromList(ListItem item, ShoppingList shoppingList) {
        if (isConnected){
            mWrappedListService.removeItemFromList(item, shoppingList);
        } else {
            queueNetworkMethod(new RemoveItemFromListMethod(item, cleanShoppingList(shoppingList)));
        }
    }

    @Override
    public void updateItemInList(ListItem item, ShoppingList shoppingList) {
        if (isConnected){
            mWrappedListService.updateItemInList(item, shoppingList);
        } else {
            queueNetworkMethod(new UpdateItemInListMethod(item, cleanShoppingList(shoppingList)));
        }
    }

    @Override
    public void deleteShoppingList(ShoppingList shoppingList) {
        if (isConnected){
            mWrappedListService.deleteShoppingList(shoppingList);
        } else {
            queueNetworkMethod(new DeleteListMethod(cleanShoppingList(shoppingList)));
        }
    }

    @Override
    public void fetchListName(ShoppingList shoppingList, NameCallback callback) {
        if (!isConnected) {
            return;
        }
        mWrappedListService.fetchListName(shoppingList, callback);
    }

    @Override
    public void fetchListItems(ShoppingList shoppingList, ItemCallback callback) {
        if (!isConnected) {
            return;
        }
        mWrappedListService.fetchListItems(shoppingList, callback);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean connected = activeNetworkInfo != null && activeNetworkInfo.isConnected();

        if (this.isConnected == connected){
            return;
        }
        this.isConnected = connected;
        Log.d(TAG, "Connectivity status: " + (isConnected?"Connected":"Not connected"));
        if (isConnected) {
            executeNetworkMethods();
        }
    }

    /**
     * Clones shopping list with no ListItems
     * This prevents us serializing unnecessary listitems
     * @param shoppingList
     * @return
     */
    private ShoppingList cleanShoppingList(ShoppingList shoppingList){
        return new ShoppingList(shoppingList.getUUID(), shoppingList.getListName(), new ArrayList<ListItem>());
    }

    private void queueNetworkMethod(NetworkMethod networkMethod){
        try{
            String method = mObjectMapper.writeValueAsString(networkMethod);
            Log.d(TAG, "Queueing " + method);

            SharedPreferences preferences = mContext.getSharedPreferences(PREFS_NAME, 0);
            int noOfEntries = preferences.getInt(PREFS_NO_ENTRIES, 0);

            preferences.edit()
                    .putString(PREFS_KEYS + noOfEntries, method)
                    .putInt(PREFS_NO_ENTRIES, ++noOfEntries)
                    .apply();
        } catch (JsonProcessingException e) {
            Log.e(TAG, "Failed to write object to json", e);
        }
    }

    private void executeNetworkMethods(){
        SharedPreferences preferences = mContext.getSharedPreferences(PREFS_NAME, 0);
        int noOfEntries = preferences.getInt(PREFS_NO_ENTRIES, 0);

        for (int i=0;i<noOfEntries;i++){
            String jsonMethod = preferences.getString(PREFS_KEYS + i, "");
            Log.d(TAG, jsonMethod);
            try {
                NetworkMethod networkMethod = mObjectMapper.readValue(jsonMethod, NetworkMethod.class);
                networkMethod.executeWith(mWrappedListService);
            } catch (IOException e) {
                Log.e(TAG, "Failed to convert the following json into NetworkMethod: " + jsonMethod, e);
            }
        }

        preferences.edit().clear().apply();
    }
}