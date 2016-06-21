package uk.co.domcampbell.shoppinglist.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import uk.co.domcampbell.shoppinglist.dto.ListItem;
import uk.co.domcampbell.shoppinglist.dto.ShoppingList;

/**
 * Created by Dominic on 14/06/16.
 */
public class ListItemWrapper extends CursorWrapper {

    public ListItemWrapper (Cursor cursor) {super(cursor);}

    public ListItem getListItem() {
        UUID uuid = UUID.fromString(getString(getColumnIndex(DbSchema.ListItemTable.Cols.UUID)));
        String itemName = getString(getColumnIndex(DbSchema.ListItemTable.Cols.ITEMNAME));
        boolean completed = getInt(getColumnIndex(DbSchema.ListItemTable.Cols.COMPLETED))>0;
        Date createdDate = new Date(getLong(getColumnIndex(DbSchema.ListItemTable.Cols.CREATEDDATE)));
        Date completedDate = new Date(getLong(getColumnIndex(DbSchema.ListItemTable.Cols.COMPLETEDDATE)));

        return new ListItem(uuid, itemName, completed, createdDate, completedDate);
    }
}
