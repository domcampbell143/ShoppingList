package uk.co.domcampbell.shoppinglist.network;


import java.util.List;

import uk.co.domcampbell.shoppinglist.dto.ListItem;
import uk.co.domcampbell.shoppinglist.dto.ShoppingList;

/**
 * Created by Dominic on 2/06/16.
 */
public interface ListService {

    void addList(ShoppingList shoppingList);

    void addUserToNetworkList(ShoppingList shoppingList);

    void updateListName(ShoppingList shoppingList);

    void addItemToList(ListItem item,ShoppingList shoppingList);

    void removeItemFromList(ListItem item,ShoppingList shoppingList);

    void updateItemInList(ListItem item,ShoppingList shoppingList);

    void deleteShoppingList(ShoppingList shoppingList);

    void fetchListName(ShoppingList shoppingList, NameCallback callback);

    void onNetworkConnectivityChange(boolean isConnected);

    interface NameCallback{
        void onNameReceived(String name);
    }

    void fetchListItems(ShoppingList shoppingList, ItemCallback callback);

    interface ItemCallback {
        void onListItemsReceived(List<ListItem> items);
    }
}
