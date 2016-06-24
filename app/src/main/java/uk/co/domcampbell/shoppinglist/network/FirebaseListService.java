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
import uk.co.domcampbell.shoppinglist.user.User;

/**
 * Created by Dominic on 20/06/16.
 */
public class FirebaseListService implements ListService {

    private static final String TAG="FirebaseListService";

    Firebase mBaseRef;
    String mUserUuidString;

    public FirebaseListService(User user){
        mUserUuidString = user.getId().toString();
        mBaseRef = new Firebase("https://dcshoppinglist.firebaseio.com/shoppinglist");
    }

    @Override
    public void addList(ShoppingList shoppingList) {
        mBaseRef.child(shoppingList.getUUID().toString()+"/name").setValue(shoppingList.getListName());
        addUserToNetworkList(shoppingList);
    }

    @Override
    public void addUserToNetworkList(ShoppingList shoppingList) {
        mBaseRef.child(shoppingList.getUUID().toString()+"/users/"+mUserUuidString).setValue(true);
    }

    @Override
    public void updateListName(ShoppingList shoppingList) {
        addList(shoppingList);
    }

    @Override
    public void deleteShoppingList(final ShoppingList shoppingList) {
        final Firebase usersRef = mBaseRef.child(shoppingList.getUUID().toString()+"/users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount()==1 && dataSnapshot.child(mUserUuidString).exists()){
                    //current user is the only one, free to delete
                    mBaseRef.child(shoppingList.getUUID().toString()).removeValue();
                } else {
                    //just delete this user from the users list (if it exists)
                    usersRef.child(mUserUuidString).removeValue();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d(TAG, "deleteShoppingList cancelled: "+firebaseError.getMessage());
            }
        });

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
    public void fetchListItems(final ShoppingList shoppingList, final ItemCallback callback) {
        Firebase listRef = mBaseRef.child(shoppingList.getUUID().toString()+"/list");
        listRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<ListItem> netListItems = new ArrayList<ListItem>();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    netListItems.add(listItemfromSnapshot(snapshot));
                }
                List<ListItem>localListItems = new ArrayList<ListItem>(shoppingList.getList());
                localListItems.removeAll(netListItems);
                for (ListItem item:localListItems){
                    callback.onItemRemoved(item);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d(TAG, "fetchListItems cancelled: "+firebaseError.getMessage());
            }
        });
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
                Log.d(TAG, "fetchListItems cancelled: "+firebaseError.getMessage());
            }
        });
    }

    @Override
    public void fetchListName(ShoppingList shoppingList, final NameCallback callback) {
        Firebase nameRef = mBaseRef.child(shoppingList.getUUID().toString()+"/name");
        nameRef.addValueEventListener(new ValueEventListener() {
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
