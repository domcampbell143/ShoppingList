package uk.co.domcampbell.shoppinglist.network;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.content.Context;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import uk.co.domcampbell.shoppinglist.dto.ListItem;
import uk.co.domcampbell.shoppinglist.dto.ShoppingList;
import uk.co.domcampbell.shoppinglist.user.User;

/**
 * Created by Dominic on 20/06/16.
 */
public class FirebaseListService implements ListService {

    private static final String TAG="FirebaseListService";

    private static final int METHOD_TYPE_SET = 0;
    private static final int METHOD_TYPE_REMOVE = 1;
    private static final String PREFS_NAME = "FirebaseListService";
    private static final String PREFS_NO_ENTRIES = "number_entries";
    private static final String PREFS_KEYS = "key_";



    private Firebase mBaseRef;
    private String mUserUuidString;
    private Context mContext;

    private boolean isConnected;

    public FirebaseListService(Context context,User user){
        mContext = context;
        mUserUuidString = user.getId().toString();
        mBaseRef = new Firebase("https://dcshoppinglist.firebaseio.com/shoppinglist");

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
        String childPath = shoppingList.getUUID().toString()+"/name";
        String value = shoppingList.getListName();
        if (isConnected) {
            mBaseRef.child(childPath).setValue(value);
        } else {
            queueNetworkMethod(childPath, value, METHOD_TYPE_SET);
        }
        addUserToNetworkList(shoppingList);
    }

    @Override
    public void addUserToNetworkList(ShoppingList shoppingList) {
        String childPath = shoppingList.getUUID().toString()+"/users/"+mUserUuidString;
        boolean value = true;
        if (isConnected) {
            mBaseRef.child(childPath).setValue(value);
        } else {
            queueNetworkMethod(childPath, value, METHOD_TYPE_SET);
        }
    }

    @Override
    public void updateListName(ShoppingList shoppingList) {
        addList(shoppingList);
    }

    @Override
    public void deleteShoppingList(final ShoppingList shoppingList) {
        String childPath = shoppingList.getUUID().toString()+"/users/"+mUserUuidString;
        if (isConnected) {
            mBaseRef.child(childPath).removeValue();
        } else {
            queueNetworkMethod(childPath, null, METHOD_TYPE_REMOVE);
        }
    }

    @Override
    public void addItemToList(ListItem item,ShoppingList shoppingList) {
        updateItemInList(item,shoppingList);
    }

    @Override
    public void removeItemFromList(ListItem item,ShoppingList shoppingList) {
        String childPath = shoppingList.getUUID().toString()+"/list/"+item.getUUID().toString();
        if (isConnected) {
            mBaseRef.child(childPath).removeValue();
        } else {
            queueNetworkMethod(childPath, null, METHOD_TYPE_REMOVE);
        }
    }

    @Override
    public void updateItemInList(ListItem item,ShoppingList shoppingList) {
        String childPath = shoppingList.getUUID().toString()+"/list/"+item.getUUID().toString();
        Map<String, Object> value = new HashMap<>();
        value.put("name", item.getItemName());
        value.put("completed", item.isCompleted());
        value.put("createdDate", item.getCreatedDate().getTime());
        if (item.getCompletedDate() != null)value.put("completedDate", item.getCompletedDate().getTime());
        if (isConnected) {
            mBaseRef.child(childPath).setValue(value);
        } else {
            queueNetworkMethod(childPath, value, METHOD_TYPE_SET);
        }
    }

    @Override
    public void fetchListItems(final ShoppingList shoppingList, final ItemCallback callback) {
        if (!isConnected) {
            return;
        }
        Firebase listRef = mBaseRef.child(shoppingList.getUUID().toString()+"/list");
        listRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<ListItem> listItems = new ArrayList<ListItem>();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    listItems.add(listItemfromSnapshot(snapshot));
                }
                callback.onListItemsReceived(listItems);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d(TAG, "fetchListItems cancelled: "+firebaseError.getMessage());
            }
        });
    }

    @Override
    public void fetchListName(ShoppingList shoppingList, final NameCallback callback) {
        if (!isConnected) {
            return;
        }
        Firebase nameRef = mBaseRef.child(shoppingList.getUUID().toString()+"/name");
        nameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callback.onNameReceived(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d(TAG, "fetchListName cancelled: "+firebaseError.getMessage());
            }
        });
    }

    @Override
    public void onNetworkConnectivityChange(boolean isConnected) {
        if (this.isConnected == isConnected){
            return;
        }
        this.isConnected = isConnected;
        Log.d(TAG, "Connectivity status: " + (isConnected?"Connected":"Not connected"));
        if (isConnected) {
            executeNetworkMethods();
        }


    }

    private ListItem listItemfromSnapshot(DataSnapshot snapshot){
        String uuid = snapshot.getKey();
        String name = snapshot.child("name").getValue(String.class);
        boolean completed = snapshot.child("completed").getValue(boolean.class);
        long createdTime = snapshot.child("createdDate").getValue(long.class);
        long completedTime = snapshot.child("completedDate").getValue(long.class);
        Date completedDate = null;
        if (completedTime != 0L) completedDate = new Date(completedTime);
        return new ListItem(UUID.fromString(uuid), name, completed, new Date(createdTime), completedDate);
    }

    private void queueNetworkMethod(String childPath, Object value, int methodType){
        NetworkMethod networkMethod = new NetworkMethod(childPath, value, methodType);
        Gson gson = new Gson();
        String method = gson.toJson(networkMethod);
        Log.d(TAG, "Queueing " + method);

        SharedPreferences preferences = mContext.getSharedPreferences(PREFS_NAME, 0);
        int noOfEntries = preferences.getInt(PREFS_NO_ENTRIES, 0);

        preferences.edit()
                .putString(PREFS_KEYS + noOfEntries, method)
                .putInt(PREFS_NO_ENTRIES, ++noOfEntries)
                .apply();
    }

    private void executeNetworkMethods(){
        SharedPreferences preferences = mContext.getSharedPreferences(PREFS_NAME, 0);
        int noOfEntries = preferences.getInt(PREFS_NO_ENTRIES, 0);

        Gson gson = new Gson();

        for (int i=0;i<noOfEntries;i++){
            String jsonMethod = preferences.getString(PREFS_KEYS + i, "");
            Log.d(TAG, jsonMethod);
            NetworkMethod networkMethod = gson.fromJson(jsonMethod, NetworkMethod.class);
            switch (networkMethod.mMethodType) {
                case METHOD_TYPE_SET:
                    mBaseRef.child(networkMethod.mChildPath).setValue(networkMethod.mValue);
                    break;
                case METHOD_TYPE_REMOVE:
                    mBaseRef.child(networkMethod.mChildPath).removeValue();
                    break;
                default:
                    break;
            }
        }

        preferences.edit().clear().apply();
    }

    private static class NetworkMethod {

        private String mChildPath;
        private Object mValue;
        private int mMethodType;

        NetworkMethod(String childPath, Object value, int methodType) {
            mChildPath = childPath;
            mValue = value;
            mMethodType =methodType;
        }
    }
}
