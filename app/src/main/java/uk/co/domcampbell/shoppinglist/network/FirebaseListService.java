package uk.co.domcampbell.shoppinglist.network;

import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import uk.co.domcampbell.shoppinglist.dto.ListItem;
import uk.co.domcampbell.shoppinglist.dto.ShoppingList;

/**
 * Created by Dominic on 20/06/16.
 */
public class FirebaseListService implements ListService {

    private static final String TAG="FirebaseListService";

    Firebase mBaseRef;

    public FirebaseListService(){
        mBaseRef = new Firebase("https://dcshoppinglist.firebaseio.com/shoppinglist");
    }

    @Override
    public void addList(ShoppingList shoppingList) {
        mBaseRef.child(shoppingList.getUUID().toString()+"/name").setValue(shoppingList.getListName());
    }

    @Override
    public void updateListName(ShoppingList shoppingList) {
        addList(shoppingList);
    }

    @Override
    public void deleteShoppingList(ShoppingList shoppingList) {
        mBaseRef.child(shoppingList.getUUID().toString()).removeValue();
    }

    @Override
    public void addItemToList(ListItem item,ShoppingList shoppingList) {
        updateItemInList(item,shoppingList);
    }

    @Override
    public void removeItemFromList(ListItem item,ShoppingList shoppingList) {
        Firebase itemRef = mBaseRef.child(shoppingList.getUUID().toString()+"/list/"+item.getUUID().toString());
        itemRef.removeValue();
    }

    @Override
    public void updateItemInList(ListItem item,ShoppingList shoppingList) {
        Firebase itemRef = mBaseRef.child(shoppingList.getUUID().toString()+"/list/"+item.getUUID().toString());
        Map<String, Object> map = new HashMap<>();
        map.put("name", item.getItemName());
        map.put("completed", item.isCompleted());
        map.put("createdDate", item.getCreatedDate().getTime());
        if (item.getCompletedDate() != null)map.put("completedDate", item.getCompletedDate().getTime());
        itemRef.setValue(map);

    }

    @Override
    public void fetchList(ShoppingList shoppingList,final Callback callback) {
        Firebase listRef = mBaseRef.child(shoppingList.getUUID().toString()+"/list");
        listRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                callback.onItemAdded(listItemfromSnapshot(dataSnapshot));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                callback.onItemChanged(listItemfromSnapshot(dataSnapshot));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                callback.onItemRemoved(listItemfromSnapshot(dataSnapshot));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d(TAG, "fetchList cancelled: "+firebaseError.getMessage());
            }
        });
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
}
