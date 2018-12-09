package uk.co.domcampbell.shoppinglist.network;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    private DatabaseReference mBaseRef;
    private String mUserUuidString;

    public FirebaseListService(User user){
        mUserUuidString = user.getId().toString();
        mBaseRef = FirebaseDatabase.getInstance().getReference().child("shoppinglist");
    }

    @Override
    public void addList(ShoppingList shoppingList) {
        String childPath = shoppingList.getUUID().toString()+"/name";
        String value = shoppingList.getListName();
        mBaseRef.child(childPath).setValue(value);
        addUserToNetworkList(shoppingList);
    }

    @Override
    public void addUserToNetworkList(ShoppingList shoppingList) {
        String childPath = shoppingList.getUUID().toString()+"/users/"+mUserUuidString;
        boolean value = true;
        mBaseRef.child(childPath).setValue(value);
    }

    @Override
    public void updateListName(ShoppingList shoppingList) {
        addList(shoppingList);
    }

    @Override
    public void deleteShoppingList(final ShoppingList shoppingList) {
        String childPath = shoppingList.getUUID().toString()+"/users/"+mUserUuidString;
        mBaseRef.child(childPath).removeValue();
    }

    @Override
    public void addItemToList(ListItem item,ShoppingList shoppingList) {
        updateItemInList(item,shoppingList);
    }

    @Override
    public void removeItemFromList(ListItem item,ShoppingList shoppingList) {
        String childPath = shoppingList.getUUID().toString()+"/list/"+item.getUUID().toString();
         mBaseRef.child(childPath).removeValue();
    }

    @Override
    public void updateItemInList(ListItem item,ShoppingList shoppingList) {
        String childPath = shoppingList.getUUID().toString()+"/list/"+item.getUUID().toString();
        Map<String, Object> value = new HashMap<>();
        value.put("name", item.getItemName());
        value.put("completed", item.isCompleted());
        value.put("createdDate", item.getCreatedDate().getTime());
        if (item.getCompletedDate() != null)value.put("completedDate", item.getCompletedDate().getTime());
        mBaseRef.child(childPath).setValue(value);
    }

    @Override
    public void fetchListItems(final ShoppingList shoppingList, final ItemCallback callback) {
        DatabaseReference listRef = mBaseRef.child(shoppingList.getUUID().toString()+"/list");
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
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "fetchListItems cancelled: "+databaseError.getMessage());
            }
        });
    }

    @Override
    public void fetchListName(ShoppingList shoppingList, final NameCallback callback) {
        DatabaseReference nameRef = mBaseRef.child(shoppingList.getUUID().toString()+"/name");
        nameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callback.onNameReceived(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "fetchListName cancelled: "+databaseError.getMessage());
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
