package uk.co.domcampbell.shoppinglist.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.List;
import java.util.UUID;

import uk.co.domcampbell.shoppinglist.dto.ListItem;
import uk.co.domcampbell.shoppinglist.dto.ShoppingList;

/**
 * Created by Dominic on 14/06/16.
 */
public class ShoppingListWrapper extends CursorWrapper{

    public ShoppingListWrapper (Cursor cursor) {super(cursor);}

    public ShoppingList getShoppingList(List<ListItem> listItems) {
        String uuidString = getString(getColumnIndex(DbSchema.ShoppingListTable.Cols.UUID));
        String name = getString(getColumnIndex(DbSchema.ShoppingListTable.Cols.NAME));

        return new ShoppingList(UUID.fromString(uuidString), name,listItems);
    }
}
