package uk.co.domcampbell.shoppinglist.network;


import java.util.List;

import uk.co.domcampbell.shoppinglist.dto.ListItem;
import uk.co.domcampbell.shoppinglist.dto.ShoppingList;

/**
 * Created by Dominic on 2/06/16.
 */
public interface ListService {

    void addList(ShoppingList shoppingList);

    void addItemToList(ListItem item,ShoppingList shoppingList);

    void removeItemFromList(ListItem item,ShoppingList shoppingList);

    void updateItemInList(ListItem item,ShoppingList shoppingList);

    void fetchList(ShoppingList shoppingList,Callback callback);

    public interface Callback {
        void onItemAdded(ListItem item);
        void onItemChanged(ListItem item);
        void onItemRemoved(ListItem item);
    }
}
