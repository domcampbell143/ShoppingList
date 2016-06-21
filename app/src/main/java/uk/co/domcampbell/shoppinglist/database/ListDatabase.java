package uk.co.domcampbell.shoppinglist.database;

import java.util.List;
import java.util.UUID;

import uk.co.domcampbell.shoppinglist.dto.ListItem;
import uk.co.domcampbell.shoppinglist.dto.ShoppingList;

/**
 * Created by Dominic on 14/06/16.
 */
public interface ListDatabase {
    void addItemToList(ListItem item, ShoppingList shoppingList);

    void removeItem(ListItem item);

    void updateItem(ListItem item);

    void addShoppingList(ShoppingList shoppingList);

    void deleteShoppingList(ShoppingList shoppingList);

    List<ShoppingList> getShoppingLists();

    ShoppingList getShoppingList(UUID uuid);
}
