package uk.co.domcampbell.shoppinglist.network;

import android.util.Log;

import java.util.ArrayList;
import java.util.Queue;

import uk.co.domcampbell.shoppinglist.dto.ListItem;
import uk.co.domcampbell.shoppinglist.dto.ShoppingList;
import uk.co.domcampbell.shoppinglist.network.method.AddItemToListMethod;
import uk.co.domcampbell.shoppinglist.network.method.AddListMethod;
import uk.co.domcampbell.shoppinglist.network.method.AddUserToListMethod;
import uk.co.domcampbell.shoppinglist.network.method.DeleteListMethod;
import uk.co.domcampbell.shoppinglist.network.method.NetworkMethod;
import uk.co.domcampbell.shoppinglist.network.method.NetworkMethodQueue;
import uk.co.domcampbell.shoppinglist.network.method.RemoveItemFromListMethod;
import uk.co.domcampbell.shoppinglist.network.method.UpdateItemInListMethod;
import uk.co.domcampbell.shoppinglist.network.method.UpdateListNameMethod;

/**
 * Created by Dominic on 31/12/2017.
 */

public class NoConnectionWrapper implements ListService {

    private static final String TAG="NoConnectionWrapper";

    private boolean isConnected;
    private ListService mWrappedListService;
    private NetworkMethodQueue mQueue;


    public NoConnectionWrapper(ListService listService, NetworkMethodQueue queue){
        mWrappedListService = listService;
        mQueue = queue;
    }

    @Override
    public void addList(ShoppingList shoppingList) {
        if (isConnected){
            mWrappedListService.addList(shoppingList);
        } else {
            mQueue.queueNetworkMethod(new AddListMethod(cleanShoppingList(shoppingList)));
        }
    }

    @Override
    public void addUserToNetworkList(ShoppingList shoppingList) {
        if (isConnected){
            mWrappedListService.addUserToNetworkList(shoppingList);
        } else {
            mQueue.queueNetworkMethod(new AddUserToListMethod(cleanShoppingList(shoppingList)));
        }
    }

    @Override
    public void updateListName(ShoppingList shoppingList) {
        if (isConnected){
            mWrappedListService.updateListName(shoppingList);
        } else {
            mQueue.queueNetworkMethod(new UpdateListNameMethod(cleanShoppingList(shoppingList)));
        }
    }

    @Override
    public void addItemToList(ListItem item, ShoppingList shoppingList) {
        if (isConnected){
            mWrappedListService.addItemToList(item, shoppingList);
        } else {
            mQueue.queueNetworkMethod(new AddItemToListMethod(item, cleanShoppingList(shoppingList)));
        }
    }

    @Override
    public void removeItemFromList(ListItem item, ShoppingList shoppingList) {
        if (isConnected){
            mWrappedListService.removeItemFromList(item, shoppingList);
        } else {
            mQueue.queueNetworkMethod(new RemoveItemFromListMethod(item, cleanShoppingList(shoppingList)));
        }
    }

    @Override
    public void updateItemInList(ListItem item, ShoppingList shoppingList) {
        if (isConnected){
            mWrappedListService.updateItemInList(item, shoppingList);
        } else {
            mQueue.queueNetworkMethod(new UpdateItemInListMethod(item, cleanShoppingList(shoppingList)));
        }
    }

    @Override
    public void deleteShoppingList(ShoppingList shoppingList) {
        if (isConnected){
            mWrappedListService.deleteShoppingList(shoppingList);
        } else {
            mQueue.queueNetworkMethod(new DeleteListMethod(cleanShoppingList(shoppingList)));
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

    /**
     * Set the connected status of the wrapper. If the status is change to connected from
     * not connected, this method will invoke any stored network actions
     * @param connected
     */
    public void setConnected(boolean connected) {
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

    private void executeNetworkMethods(){
        Queue<NetworkMethod> queue = mQueue.getQueuedMethods();
        while (!queue.isEmpty()){
            NetworkMethod networkMethod = queue.remove();
            networkMethod.executeWith(mWrappedListService);
        }
    }
}
